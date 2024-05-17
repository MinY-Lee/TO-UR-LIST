# TOURLIST 포팅 매뉴얼
## 환경 설명
- TOURLIST는 한대의 독립적인 CICD 서버와 AWS EKS와 컨트롤 노드 역할을 하는 하나의 EC2 서버를 통해 구동합니다.
- 해당 구조는 EKS 환경이 유지된다는 가정하에 나머지 서버의 경우 상황에 맞게 수정할 수 있습니다.
## CICD 서버
- 해당 서버에는 jenkins를 사용하여 CICD 환경을 구축합니다.
### 서버 기본 세팅
```bash
sudo timedatectl set-timezone Asia/Seoul
```

```bash
sudo sed -i 's/ap-northeast-2.ec2.archive.ubuntu.com/mirror.kakao.com/g' /etc/apt/sources.list
```

```bash
sudo apt-get -y update
```

```bash
sudo apt-get -y upgrade
```

```bash
sudo swapoff -a
```

```bash
sudo sed -i '/ swap / s/^/#/' /etc/fstab
```

```bash
sudo hostnamectl set-hostname "master-node"
```

```bash
sudo apt-get -y install apt-transport-https ca-certificates curl gnupg-agent software-properties-common
```
### docker 설치

```bash
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

```bash
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
```

```bash
sudo apt-get -y update
```

```bash
sudo apt-get -y install docker-ce docker-ce-cli containerd.io
```

```bash
sudo usermod -aG docker ubuntu
```

```bash
sudo service docker restart
```

```bash
exit
```
### Docker-Compose 설치

```bash
sudo curl -L "https://github.com/docker/compose/releases/download/v2.21.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
```

```bash
sudo chmod +x /usr/local/bin/docker-compose
```

### Jenkins 실행

```bash
docker pull jenkins/jenkins:jdk17
```

```bash
docker run -d --restart always --env JENKINS_OPTS=--httpPort=8080 -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul -p 8080:8080 -v /jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /usr/local/bin/docker-compose:/usr/local/bin/docker-compose --name jenkins -u root jenkins/jenkins:jdk17
```

```bash
groupadd -f docker
```

```bash
usermod -aG docker jenkins
```

```bash
chown root:docker /var/run/docker.sock
```

```bash
curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
```

```bash
chmod +x /usr/local/bin/docker-compose
```
### Jenkins Pipline
- gateway
    
    ```groovy
    pipeline {
        agent any
        
        environment {
            backendImageName = 'ssuyas/tourlist_gateway'
            registryCredential = 'ssuyas-docker'
            
            controlNodeAccount = [control node 원격 접속 계정명 ex)'ec2-user']
            controlNodeUri = [control node 원격 접속 주소]
            
            serviceName = 'gateway'
            k8sDir = "/home/${controlNodeAccount}/tourlist/${serviceName}" // Kubernetes 디렉터리 경로
            localPath = "kubernetes/${serviceName}"
            serviceYamlLocalPath = "${localPath}/tourlist-${serviceName}-service.yml" // git repo 내 서비스 YAML 파일 경로
            serviceYamlPath = "${k8sDir}/tourlist-${serviceName}-service.yml" // 서비스 YAML 파일 경로
            secretsYamlPath = "${k8sDir}/tourlist-${serviceName}-secrets.yml" // 시크릿 YAML 파일 경로
            applyAllScriptLocalPath = "kubernetes/apply-all.sh" // git repo 내 apply-all.sh 스크립트 파일 경로
            applyAllScriptPath = "${k8sDir}/apply-all.sh" // apply-all.sh 스크립트 파일 경로
            serviceSecretId = "tourlist-${serviceName}-secrets"
            serviceDbSecretId = "tourlist-${serviceName}-db-secrets"
        }
        
        tools {
            gradle 'gradle8.7'
            nodejs 'nodejs18.19'
        }
            
        stages {
            stage('Git Clone') {
                steps {
                    git branch: 'deploy',
                        credentialsId: 'treamor-gitlab',
                        url: 'https://lab.ssafy.com/s10-final/S10P31A609'
                }
            }
            
            stage('Backend Jar Build') {
                steps {
                    dir (serviceName) {
                        sh 'gradle clean bootjar'
                    }
                }
            }
            
            stage('Backend Image Build & DockerHub Push') {
                steps {
                    dir(serviceName) {
                        script {
                            docker.withRegistry('', registryCredential) {
                                sh "docker buildx create --use --name mybuilder"
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $backendImageName:$BUILD_NUMBER --push ."
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $backendImageName:latest --push ."
                            }
                        }
                    }
                }
            }
            
            stage("Override Docker Image Tag") {
                steps {
                    script {
                        sh "sed -i 's| $backendImageName:latest| $backendImageName:$BUILD_NUMBER|' $serviceYamlLocalPath"
                    }
                }
            }
            
            stage("Copy k8s yaml To worker node") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh "scp -o StrictHostKeyChecking=no -r $localPath/* $controlNodeAccount@$controlNodeUri:$k8sDir"
                    }
                }
            }
            
            stage("Copy Secrets yaml To worker node") {
                steps {
                    withCredentials([file(credentialsId: serviceSecretId, variable: 'secrets')]) {
                        sshagent(credentials: ['worker-node']) {
                            sh'''
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsYamlPath || true"
                            scp -o StrictHostKeyChecking=no $secrets $controlNodeAccount@$controlNodeUri:$secretsYamlPath
                            '''
                        }
                    }
                }
            }
            
            stage("Apply All") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no $applyAllScriptLocalPath $controlNodeAccount@$controlNodeUri:$applyAllScriptPath
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "sudo chmod 755 $applyAllScriptPath"
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "$applyAllScriptPath $k8sDir"
                        '''
                    }
                }
            }
        }
    }
    ```
- user
    
    ```groovy
    pipeline {
        agent any
        
        environment {
            backendImageName = 'ssuyas/tourlist_user'
            registryCredential = 'ssuyas-docker'
            
            controlNodeAccount = [control node 원격 접속 계정명 ex)'ec2-user']
            controlNodeUri = [control node 원격 접속 주소]
            
            serviceName = 'user'
            k8sDir = "/home/${controlNodeAccount}/tourlist/${serviceName}" // Kubernetes 디렉터리 경로
            localPath = "kubernetes/${serviceName}"
            serviceYamlLocalPath = "${localPath}/tourlist-${serviceName}-service.yml" // git repo 내 서비스 YAML 파일 경로
            secretsYamlPath = "${k8sDir}/tourlist-${serviceName}-secrets.yml" // 시크릿 YAML 파일 경로
            secretsDbYamlPath = "${k8sDir}/tourlist-${serviceName}-db-secrets.yml"
            localInitDbFilePath = "docker-compose/${serviceName}/initdb.d" // git repo 내 서비스 init db 경로
            initDbFilePath = "${k8sDir}/initdb" // init db 경로
            applyAllScriptLocalPath = "kubernetes/apply-all.sh" // git repo 내 apply-all.sh 스크립트 파일 경로
            applyAllScriptPath = "${k8sDir}/apply-all.sh" // apply-all.sh 스크립트 파일 경로
            createConfigMapLocalPath = "kubernetes/create-configmap.sh"
            createConfigMapPath = "${k8sDir}/create-configmap.sh"
            serviceSecretId = "tourlist-${serviceName}-secrets"
            serviceDbSecretId = "tourlist-${serviceName}-db-secrets"
            configMapName = "tourlist-${serviceName}-initdb-config"
        }
        
        tools {
            gradle 'gradle8.7'
            nodejs 'nodejs18.19'
        }
            
        stages {
            stage('Git Clone') {
                steps {
                    git branch: 'deploy',
                        credentialsId: 'treamor-gitlab',
                        url: 'https://lab.ssafy.com/s10-final/S10P31A609'
                }
            }
            
            stage('Backend Jar Build') {
                steps {
                    dir (serviceName) {
                        sh 'gradle clean bootjar'
                    }
                }
            }
            
            stage('Backend Image Build & DockerHub Push') {
                steps {
                    dir(serviceName) {
                        script {
                            docker.withRegistry('', registryCredential) {
                                sh "docker buildx create --use --name mybuilder"
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $backendImageName:$BUILD_NUMBER --push ."
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $backendImageName:latest --push ."
                            }
                        }
                    }
                }
            }
            
            stage("Override Docker Image Tag") {
                steps {
                    script {
                        sh "sed -i 's| $backendImageName:latest| $backendImageName:$BUILD_NUMBER|' $serviceYamlLocalPath"
                    }
                }
            }
            
            stage("Copy k8s yaml To worker node") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh "scp -o StrictHostKeyChecking=no -r $localPath/* $controlNodeAccount@$controlNodeUri:$k8sDir"
                    }
                }
            }
            
            stage("Copy Secrets yaml To worker node") {
                steps {
                    withCredentials([file(credentialsId: serviceSecretId, variable: 'secrets'), file(credentialsId: serviceDbSecretId, variable: 'dbsecrets')]) {
                        sshagent(credentials: ['worker-node']) {
                            sh'''
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsYamlPath || true"
                            scp -o StrictHostKeyChecking=no $secrets $controlNodeAccount@$controlNodeUri:$secretsYamlPath
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsDbYamlPath || true"
                            scp -o StrictHostKeyChecking=no $dbsecrets $controlNodeAccount@$controlNodeUri:$secretsDbYamlPath
                            '''
                        }
                    }
                }
            }
            
            stage("Copy Initdb Files To worker node") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no -r $localInitDbFilePath/* $controlNodeAccount@$controlNodeUri:$initDbFilePath
                        '''
                    }
                }
            }
            
            stage("Create Config Map Yaml & apply") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no -r $createConfigMapLocalPath $controlNodeAccount@$controlNodeUri:$createConfigMapPath
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "sudo chmod 755 $createConfigMapPath"
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "$createConfigMapPath $initDbFilePath $k8sDir $configMapName"
                        '''
                    }
                }
            }
            
            stage("Apply All") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no $applyAllScriptLocalPath $controlNodeAccount@$controlNodeUri:$applyAllScriptPath
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "sudo chmod 755 $applyAllScriptPath"
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "$applyAllScriptPath $k8sDir"
                        '''
                    }
                }
            }
        }
    }
    
    ```
    
- frontend
    
    ```groovy
    pipeline {
        agent any
        
        environment {
            frontendImageName = 'ssuyas/tourlist_frontend'
            registryCredential = 'ssuyas-docker'
            
            controlNodeAccount = [control node 원격 접속 계정명 ex)'ec2-user']
            controlNodeUri = [control node 원격 접속 주소]
            
            serviceName = 'frontend'
            k8sDir = "/home/${controlNodeAccount}/tourlist/${serviceName}" // Kubernetes 디렉터리 경로
            localPath = "kubernetes/${serviceName}"
            serviceYamlLocalPath = "${localPath}/tourlist-${serviceName}-service.yml" // git repo 내 서비스 YAML 파일 경로
            serviceYamlPath = "${k8sDir}/tourlist-${serviceName}-service.yml" // 서비스 YAML 파일 경로
            secretsYamlPath = "${k8sDir}/tourlist-${serviceName}-secrets.yml" // 시크릿 YAML 파일 경로
            applyAllScriptLocalPath = "kubernetes/apply-all.sh" // git repo 내 apply-all.sh 스크립트 파일 경로
            applyAllScriptPath = "${k8sDir}/apply-all.sh" // apply-all.sh 스크립트 파일 경로
            serviceSecretId = "tourlist-${serviceName}-secrets"
            serviceDbSecretId = "tourlist-${serviceName}-db-secrets"
        }
        
        tools {
            gradle 'gradle8.7'
            nodejs 'nodejs18.19'
        }
            
        stages {
            stage('Git Clone') {
                steps {
                    git branch: 'deploy',
                        credentialsId: 'treamor-gitlab',
                        url: 'https://lab.ssafy.com/s10-final/S10P31A609'
                }
            }
            
            stage('Node Build') {
                steps {
                    dir (serviceName) {
                        withCredentials([file(credentialsId:'dotenv', variable: 'dotenv')]) {
                            sh 'cp ${dotenv} ../'
                            sh 'npm add @rollup/rollup-linux-x64-gnu'
                            sh 'npm add @esbuild/linux-x64 --omit=optional'
                            sh 'npm install'
                            sh 'npm run build'
                        }
                    }
                }
            }
            
            stage('Front Image Build & DockerHub Push') {
                steps {
                    dir(serviceName) {
                        script {
                            docker.withRegistry('', registryCredential) {
                                sh "docker buildx create --use --name mybuilder"
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $frontendImageName:$BUILD_NUMBER --push ."
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $frontendImageName:latest --push ."
                            }
                        }
                    }
                }
            }
            
            stage("Override Docker Image Tag") {
                steps {
                    script {
                        sh "sed -i 's| $frontendImageName:latest| $frontendImageName:$BUILD_NUMBER|' $serviceYamlLocalPath"
                    }
                }
            }
            
            stage("Copy k8s yaml To worker node") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh "scp -o StrictHostKeyChecking=no -r $localPath/* $controlNodeAccount@$controlNodeUri:$k8sDir"
                    }
                }
            }
            
            stage("Apply All") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no $applyAllScriptLocalPath $controlNodeAccount@$controlNodeUri:$applyAllScriptPath
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "sudo chmod 755 $applyAllScriptPath"
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "$applyAllScriptPath $k8sDir"
                        '''
                    }
                }
            }
        }
    }
    ```
    
- common
    
    ```groovy
    pipeline {
        agent any
        
        environment {        
            controlNodeAccount = [control node 원격 접속 계정명 ex)'ec2-user']
            controlNodeUri = [control node 원격 접속 주소]
            
            serviceName = 'common'
            k8sDir = "/home/${controlNodeAccount}/tourlist/${serviceName}" // Kubernetes 디렉터리 경로
            localPath = "kubernetes/${serviceName}"
            serviceYamlLocalPath = "${localPath}/tourlist-${serviceName}-service.yml" // git repo 내 서비스 YAML 파일 경로
            secretsYamlPath = "${k8sDir}/tourlist-${serviceName}-secrets.yml" // 시크릿 YAML 파일 경로
            secretsDbYamlPath = "${k8sDir}/tourlist-${serviceName}-db-secrets.yml"
            localInitDbFilePath = "docker-compose/${serviceName}/initdb.d" // git repo 내 서비스 init db 경로
            initDbFilePath = "${k8sDir}/initdb" // init db 경로
            applyAllScriptLocalPath = "kubernetes/apply-all.sh" // git repo 내 apply-all.sh 스크립트 파일 경로
            applyAllScriptPath = "${k8sDir}/apply-all.sh" // apply-all.sh 스크립트 파일 경로
            createConfigMapLocalPath = "kubernetes/create-configmap.sh"
            createConfigMapPath = "${k8sDir}/create-configmap.sh"
            serviceSecretId = "tourlist-${serviceName}-secrets"
            serviceDbSecretId = "tourlist-${serviceName}-db-secrets"
            configMapName = "tourlist-${serviceName}-initdb-config"
        }
        
        tools {
            gradle 'gradle8.7'
            nodejs 'nodejs18.19'
        }
            
        stages {
            stage('Git Clone') {
                steps {
                    git branch: 'deploy',
                        credentialsId: 'treamor-gitlab',
                        url: 'https://lab.ssafy.com/s10-final/S10P31A609'
                }
            }
            
            stage("Copy k8s yaml To worker node") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh "scp -o StrictHostKeyChecking=no -r $localPath/* $controlNodeAccount@$controlNodeUri:$k8sDir"
                    }
                }
            }
            
            stage("Copy Secrets yaml To worker node") {
                steps {
                    withCredentials([file(credentialsId: serviceSecretId, variable: 'secrets'), file(credentialsId: serviceDbSecretId, variable: 'dbsecrets')]) {
                        sshagent(credentials: ['worker-node']) {
                            sh'''
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsYamlPath || true"
                            scp -o StrictHostKeyChecking=no $secrets $controlNodeAccount@$controlNodeUri:$secretsYamlPath
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsDbYamlPath || true"
                            scp -o StrictHostKeyChecking=no $dbsecrets $controlNodeAccount@$controlNodeUri:$secretsDbYamlPath
                            '''
                        }
                    }
                }
            }
            
            stage("Copy Initdb Files To worker node") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no -r $localInitDbFilePath/* $controlNodeAccount@$controlNodeUri:$initDbFilePath
                        '''
                    }
                }
            }
            
            stage("Create Config Map Yaml & apply") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no -r $createConfigMapLocalPath $controlNodeAccount@$controlNodeUri:$createConfigMapPath
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "sudo chmod 755 $createConfigMapPath"
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "$createConfigMapPath $initDbFilePath $k8sDir $configMapName"
                        '''
                    }
                }
            }
            
            stage("Apply All") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no $applyAllScriptLocalPath $controlNodeAccount@$controlNodeUri:$applyAllScriptPath
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "sudo chmod 755 $applyAllScriptPath"
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "$applyAllScriptPath $k8sDir"
                        '''
                    }
                }
            }
        }
    }
    
    ```
    
- payment
    
    ```groovy
    pipeline {
        agent any
        
        environment {
            backendImageName = 'ssuyas/tourlist_payment'
            registryCredential = 'ssuyas-docker'
            
            controlNodeAccount = [control node 원격 접속 계정명 ex)'ec2-user']
            controlNodeUri = [control node 원격 접속 주소]
            
            serviceName = 'payment'
            k8sDir = "/home/${controlNodeAccount}/tourlist/${serviceName}" // Kubernetes 디렉터리 경로
            localPath = "kubernetes/${serviceName}"
            serviceYamlLocalPath = "${localPath}/tourlist-${serviceName}-service.yml" // git repo 내 서비스 YAML 파일 경로
            secretsYamlPath = "${k8sDir}/tourlist-${serviceName}-secrets.yml" // 시크릿 YAML 파일 경로
            secretsDbYamlPath = "${k8sDir}/tourlist-${serviceName}-db-secrets.yml"
            localInitDbFilePath = "docker-compose/${serviceName}/initdb.d" // git repo 내 서비스 init db 경로
            initDbFilePath = "${k8sDir}/initdb" // init db 경로
            applyAllScriptLocalPath = "kubernetes/apply-all.sh" // git repo 내 apply-all.sh 스크립트 파일 경로
            applyAllScriptPath = "${k8sDir}/apply-all.sh" // apply-all.sh 스크립트 파일 경로
            createConfigMapLocalPath = "kubernetes/create-configmap.sh"
            createConfigMapPath = "${k8sDir}/create-configmap.sh"
            serviceSecretId = "tourlist-${serviceName}-secrets"
            serviceDbSecretId = "tourlist-${serviceName}-db-secrets"
            configMapName = "tourlist-${serviceName}-initdb-config"
        }
        
        tools {
            gradle 'gradle8.7'
            nodejs 'nodejs18.19'
        }
            
        stages {
            stage('Git Clone') {
                steps {
                    git branch: 'deploy',
                        credentialsId: 'treamor-gitlab',
                        url: 'https://lab.ssafy.com/s10-final/S10P31A609'
                }
            }
            
            stage('Backend Jar Build') {
                steps {
                    dir (serviceName) {
                        sh 'gradle clean bootjar'
                    }
                }
            }
            
            stage('Backend Image Build & DockerHub Push') {
                steps {
                    dir(serviceName) {
                        script {
                            docker.withRegistry('', registryCredential) {
                                sh "docker buildx create --use --name mybuilder"
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $backendImageName:$BUILD_NUMBER --push ."
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $backendImageName:latest --push ."
                            }
                        }
                    }
                }
            }
            
            stage("Override Docker Image Tag") {
                steps {
                    script {
                        sh "sed -i 's| $backendImageName:latest| $backendImageName:$BUILD_NUMBER|' $serviceYamlLocalPath"
                    }
                }
            }
            
            stage("Copy k8s yaml To worker node") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh "scp -o StrictHostKeyChecking=no -r $localPath/* $controlNodeAccount@$controlNodeUri:$k8sDir"
                    }
                }
            }
            
            stage("Copy Secrets yaml To worker node") {
                steps {
                    withCredentials([file(credentialsId: serviceSecretId, variable: 'secrets'), file(credentialsId: serviceDbSecretId, variable: 'dbsecrets')]) {
                        sshagent(credentials: ['worker-node']) {
                            sh'''
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsYamlPath || true"
                            scp -o StrictHostKeyChecking=no $secrets $controlNodeAccount@$controlNodeUri:$secretsYamlPath
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsDbYamlPath || true"
                            scp -o StrictHostKeyChecking=no $dbsecrets $controlNodeAccount@$controlNodeUri:$secretsDbYamlPath
                            '''
                        }
                    }
                }
            }
            
            stage("Copy Initdb Files To worker node") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no -r $localInitDbFilePath/* $controlNodeAccount@$controlNodeUri:$initDbFilePath
                        '''
                    }
                }
            }
            
            stage("Create Config Map Yaml & apply") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no -r $createConfigMapLocalPath $controlNodeAccount@$controlNodeUri:$createConfigMapPath
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "sudo chmod 755 $createConfigMapPath"
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "$createConfigMapPath $initDbFilePath $k8sDir $configMapName"
                        '''
                    }
                }
            }
            
            stage("Apply All") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no $applyAllScriptLocalPath $controlNodeAccount@$controlNodeUri:$applyAllScriptPath
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "sudo chmod 755 $applyAllScriptPath"
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "$applyAllScriptPath $k8sDir"
                        '''
                    }
                }
            }
        }
    }
    
    ```
    
- place
    
    ```groovy
    pipeline {
        agent any
        
        environment {
            backendImageName = 'ssuyas/tourlist_place'
            registryCredential = 'ssuyas-docker'
            
            controlNodeAccount = [control node 원격 접속 계정명 ex)'ec2-user']
            controlNodeUri = [control node 원격 접속 주소]
            
            serviceName = 'place'
            k8sDir = "/home/${controlNodeAccount}/tourlist/${serviceName}" // Kubernetes 디렉터리 경로
            localPath = "kubernetes/${serviceName}"
            serviceYamlLocalPath = "${localPath}/tourlist-${serviceName}-service.yml" // git repo 내 서비스 YAML 파일 경로
            secretsYamlPath = "${k8sDir}/tourlist-${serviceName}-secrets.yml" // 시크릿 YAML 파일 경로
            secretsDbYamlPath = "${k8sDir}/tourlist-${serviceName}-db-secrets.yml"
            localInitDbFilePath = "docker-compose/${serviceName}/initdb.d" // git repo 내 서비스 init db 경로
            initDbFilePath = "${k8sDir}/initdb" // init db 경로
            applyAllScriptLocalPath = "kubernetes/apply-all.sh" // git repo 내 apply-all.sh 스크립트 파일 경로
            applyAllScriptPath = "${k8sDir}/apply-all.sh" // apply-all.sh 스크립트 파일 경로
            createConfigMapLocalPath = "kubernetes/create-configmap.sh"
            createConfigMapPath = "${k8sDir}/create-configmap.sh"
            serviceSecretId = "tourlist-${serviceName}-secrets"
            serviceDbSecretId = "tourlist-${serviceName}-db-secrets"
            configMapName = "tourlist-${serviceName}-initdb-config"
        }
        
        tools {
            gradle 'gradle8.7'
            nodejs 'nodejs18.19'
        }
            
        stages {
            stage('Git Clone') {
                steps {
                    git branch: 'deploy',
                        credentialsId: 'treamor-gitlab',
                        url: 'https://lab.ssafy.com/s10-final/S10P31A609'
                }
            }
            
            stage('Backend Jar Build') {
                steps {
                    dir (serviceName) {
                        sh 'gradle clean bootjar'
                    }
                }
            }
            
            stage('Backend Image Build & DockerHub Push') {
                steps {
                    dir(serviceName) {
                        script {
                            docker.withRegistry('', registryCredential) {
                                sh "docker buildx create --use --name mybuilder"
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $backendImageName:$BUILD_NUMBER --push ."
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $backendImageName:latest --push ."
                            }
                        }
                    }
                }
            }
            
            stage("Override Docker Image Tag") {
                steps {
                    script {
                        sh "sed -i 's| $backendImageName:latest| $backendImageName:$BUILD_NUMBER|' $serviceYamlLocalPath"
                    }
                }
            }
            
            stage("Copy k8s yaml To worker node") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh "scp -o StrictHostKeyChecking=no -r $localPath/* $controlNodeAccount@$controlNodeUri:$k8sDir"
                    }
                }
            }
            
            stage("Copy Secrets yaml To worker node") {
                steps {
                    withCredentials([file(credentialsId: serviceSecretId, variable: 'secrets'), file(credentialsId: serviceDbSecretId, variable: 'dbsecrets')]) {
                        sshagent(credentials: ['worker-node']) {
                            sh'''
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsYamlPath || true"
                            scp -o StrictHostKeyChecking=no $secrets $controlNodeAccount@$controlNodeUri:$secretsYamlPath
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsDbYamlPath || true"
                            scp -o StrictHostKeyChecking=no $dbsecrets $controlNodeAccount@$controlNodeUri:$secretsDbYamlPath
                            '''
                        }
                    }
                }
            }
            
            stage("Apply All") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no $applyAllScriptLocalPath $controlNodeAccount@$controlNodeUri:$applyAllScriptPath
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "sudo chmod 755 $applyAllScriptPath"
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "$applyAllScriptPath $k8sDir"
                        '''
                    }
                }
            }
        }
    }
    
    ```
    
- tour
    
    ```groovy
    pipeline {
        agent any
        
        environment {
            backendImageName = 'ssuyas/tourlist_place'
            registryCredential = 'ssuyas-docker'
            
            controlNodeAccount = [control node 원격 접속 계정명 ex)'ec2-user']
            controlNodeUri = [control node 원격 접속 주소]
            
            serviceName = 'tour'
            k8sDir = "/home/${controlNodeAccount}/tourlist/${serviceName}" // Kubernetes 디렉터리 경로
            localPath = "kubernetes/${serviceName}"
            serviceYamlLocalPath = "${localPath}/tourlist-${serviceName}-service.yml" // git repo 내 서비스 YAML 파일 경로
            secretsYamlPath = "${k8sDir}/tourlist-${serviceName}-secrets.yml" // 시크릿 YAML 파일 경로
            secretsDbYamlPath = "${k8sDir}/tourlist-${serviceName}-db-secrets.yml"
            localInitDbFilePath = "docker-compose/${serviceName}/initdb.d" // git repo 내 서비스 init db 경로
            initDbFilePath = "${k8sDir}/initdb" // init db 경로
            applyAllScriptLocalPath = "kubernetes/apply-all.sh" // git repo 내 apply-all.sh 스크립트 파일 경로
            applyAllScriptPath = "${k8sDir}/apply-all.sh" // apply-all.sh 스크립트 파일 경로
            createConfigMapLocalPath = "kubernetes/create-configmap.sh"
            createConfigMapPath = "${k8sDir}/create-configmap.sh"
            serviceSecretId = "tourlist-${serviceName}-secrets"
            serviceDbSecretId = "tourlist-${serviceName}-db-secrets"
            configMapName = "tourlist-${serviceName}-initdb-config"
        }
        
        tools {
            gradle 'gradle8.7'
            nodejs 'nodejs18.19'
        }
            
        stages {
            stage('Git Clone') {
                steps {
                    git branch: 'deploy',
                        credentialsId: 'treamor-gitlab',
                        url: 'https://lab.ssafy.com/s10-final/S10P31A609'
                }
            }
            
            stage('Backend Jar Build') {
                steps {
                    dir (serviceName) {
                        sh 'gradle clean bootjar'
                    }
                }
            }
            
            stage('Backend Image Build & DockerHub Push') {
                steps {
                    dir(serviceName) {
                        script {
                            docker.withRegistry('', registryCredential) {
                                sh "docker buildx create --use --name mybuilder"
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $backendImageName:$BUILD_NUMBER --push ."
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $backendImageName:latest --push ."
                            }
                        }
                    }
                }
            }
            
            stage("Override Docker Image Tag") {
                steps {
                    script {
                        sh "sed -i 's| $backendImageName:latest| $backendImageName:$BUILD_NUMBER|' $serviceYamlLocalPath"
                    }
                }
            }
            
            stage("Copy k8s yaml To worker node") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh "scp -o StrictHostKeyChecking=no -r $localPath/* $controlNodeAccount@$controlNodeUri:$k8sDir"
                    }
                }
            }
            
            stage("Copy Secrets yaml To worker node") {
                steps {
                    withCredentials([file(credentialsId: serviceSecretId, variable: 'secrets'), file(credentialsId: serviceDbSecretId, variable: 'dbsecrets')]) {
                        sshagent(credentials: ['worker-node']) {
                            sh'''
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsYamlPath || true"
                            scp -o StrictHostKeyChecking=no $secrets $controlNodeAccount@$controlNodeUri:$secretsYamlPath
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsDbYamlPath || true"
                            scp -o StrictHostKeyChecking=no $dbsecrets $controlNodeAccount@$controlNodeUri:$secretsDbYamlPath
                            '''
                        }
                    }
                }
            }
            
            stage("Apply All") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no $applyAllScriptLocalPath $controlNodeAccount@$controlNodeUri:$applyAllScriptPath
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "sudo chmod 755 $applyAllScriptPath"
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "$applyAllScriptPath $k8sDir"
                        '''
                    }
                }
            }
        }
    }
    
    ```
    
- checklist
    
    ```groovy
    pipeline {
        agent any
        
        environment {
            backendImageName = 'ssuyas/tourlist_checklist'
            registryCredential = 'ssuyas-docker'
            
            controlNodeAccount = [control node 원격 접속 계정명 ex)'ec2-user']
            controlNodeUri = [control node 원격 접속 주소]
            
            serviceName = 'checklist'
            k8sDir = "/home/${controlNodeAccount}/tourlist/${serviceName}" // Kubernetes 디렉터리 경로
            localPath = "kubernetes/${serviceName}"
            serviceYamlLocalPath = "${localPath}/tourlist-${serviceName}-service.yml" // git repo 내 서비스 YAML 파일 경로
            secretsYamlPath = "${k8sDir}/tourlist-${serviceName}-secrets.yml" // 시크릿 YAML 파일 경로
            secretsDbYamlPath = "${k8sDir}/tourlist-${serviceName}-db-secrets.yml"
            localInitDbFilePath = "docker-compose/${serviceName}/import" // git repo 내 서비스 init db 경로
            initDbFilePath = "${k8sDir}/import" // init db 경로
            applyAllScriptLocalPath = "kubernetes/apply-all.sh" // git repo 내 apply-all.sh 스크립트 파일 경로
            applyAllScriptPath = "${k8sDir}/apply-all.sh" // apply-all.sh 스크립트 파일 경로
            createConfigMapLocalPath = "kubernetes/create-configmap.sh"
            createConfigMapPath = "${k8sDir}/create-configmap.sh"
            serviceSecretId = "tourlist-${serviceName}-secrets"
            serviceDbSecretId = "tourlist-${serviceName}-db-secrets"
            configMapName = "tourlist-${serviceName}-initdb-config"
        }
        
        tools {
            gradle 'gradle8.7'
            nodejs 'nodejs18.19'
        }
            
        stages {
            stage('Git Clone') {
                steps {
                    git branch: 'deploy',
                        credentialsId: 'treamor-gitlab',
                        url: 'https://lab.ssafy.com/s10-final/S10P31A609'
                }
            }
            
            stage('Backend Jar Build') {
                steps {
                    dir (serviceName) {
                        sh 'gradle clean bootjar'
                    }
                }
            }
            
            stage('Backend Image Build & DockerHub Push') {
                steps {
                    dir(serviceName) {
                        script {
                            docker.withRegistry('', registryCredential) {
                                sh "docker buildx create --use --name mybuilder"
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $backendImageName:$BUILD_NUMBER --push ."
                                sh "docker buildx build --platform linux/amd64,linux/arm64 -t $backendImageName:latest --push ."
                            }
                        }
                    }
                }
            }
            
            stage("Override Docker Image Tag") {
                steps {
                    script {
                        sh "sed -i 's| $backendImageName:latest| $backendImageName:$BUILD_NUMBER|' $serviceYamlLocalPath"
                    }
                }
            }
            
            stage("Copy k8s yaml To worker node") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh "scp -o StrictHostKeyChecking=no -r $localPath/* $controlNodeAccount@$controlNodeUri:$k8sDir"
                    }
                }
            }
            
            stage("Copy Secrets yaml To worker node") {
                steps {
                    withCredentials([file(credentialsId: serviceSecretId, variable: 'secrets'), file(credentialsId: serviceDbSecretId, variable: 'dbsecrets')]) {
                        sshagent(credentials: ['worker-node']) {
                            sh'''
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsYamlPath || true"
                            scp -o StrictHostKeyChecking=no $secrets $controlNodeAccount@$controlNodeUri:$secretsYamlPath
                            ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "chmod 644 $secretsDbYamlPath || true"
                            scp -o StrictHostKeyChecking=no $dbsecrets $controlNodeAccount@$controlNodeUri:$secretsDbYamlPath
                            '''
                        }
                    }
                }
            }
            
            stage("Copy Initdb Files To worker node") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no -r $localInitDbFilePath/* $controlNodeAccount@$controlNodeUri:$initDbFilePath
                        '''
                    }
                }
            }
            
            stage("Create Config Map Yaml & apply") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no -r $createConfigMapLocalPath $controlNodeAccount@$controlNodeUri:$createConfigMapPath
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "sudo chmod 755 $createConfigMapPath"
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "$createConfigMapPath $initDbFilePath $k8sDir $configMapName"
                        '''
                    }
                }
            }
            
            stage("Apply All") {
                steps {
                    sshagent(credentials: ['worker-node']) {
                        sh '''
                        scp -o StrictHostKeyChecking=no $applyAllScriptLocalPath $controlNodeAccount@$controlNodeUri:$applyAllScriptPath
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "sudo chmod 755 $applyAllScriptPath"
                        ssh -o StrictHostKeyChecking=no $controlNodeAccount@$controlNodeUri "$applyAllScriptPath $k8sDir"
                        '''
                    }
                }
            }
        }
    }
    
    ```
---
  
## 운영 서버
### 네트워크 설정
#### VPC
- 생성할 리소스: VPC만
- 이름 태그: tourlist-EKS-VPC
- IPv4 CIDR 블록: IPv4 CIDR 수동 입력
- IPv4 CIDR: 192.168.0.0/16
- IPv6 CIDR 블록: IPv6 CIDR 블록 없음
#### Sub Net
- 서브넷 이릅: tourlist-PUBLIC-NAT / tourlist-PUBLIC-BASTION / tourlist-PRIVATE-A / tourlist-PRIVATE-C
- 가용 영역: 아시아 태평양(서울) ap-northeast-2a / 2c / 2a / 2c
- 네트워크 대역 : 192.168.10.0/24 / 192.168.11.0/24 / 192.168.12.0/24 /192.168.13.0/24 
#### Internet GateWay
- 인터넷 게이트웨이 생성
- 이름 태그 : tourlist-IGW
- VPC 연결 : 위에서 생성한 VPC에 연결
#### NAT GateWay
- 이름 : tourlist-PUBLIC-NAT-GW
- 서브넷 : 위에서 설정한 PUBLIC-NAT 서브넷에 연결
- 연결 유형 : public
- 탄력적 IP 할당 : 없다면 새로 발급받아 할당.
#### RouteTable
- 라우팅 테이블 생성
- 이름 : tourlist-IGW-ROUTETABLE
- VPC : 위에서 생성한 VPC
- 내부와 외부로 나가는 모든 주소에 tourlist-IGW로 향하도록 설정
- 위에서 생성한 PUBLIC 서브넷 연결
- 이름 : tourlist-NAT-ROUTETABLE
- VPC : 위에서 생성한 VPC
- 내부와 외부로 나가는 모든 주소에 tourlist-NAT-GW로 향하도록 설정
- 쿠버네티스 환경이 위치할 PRIVATE 서브넷 연결
- tourlist-NAT-ROUTETABLE을 기본 라우팅 테이블로 설정후 자동 생성된 default 라우팅 테이블 삭제
### BASTION host 설정
#### 보안 그룹 & 키 페어
- 키페어 생성 : RSA, .pem 생성
- 보안 그룹 생성
- 보안 그룹 이름 : tourlist=BASTIONHOST-SG
- VPC : 위에서 생성한 VPC에 연결
- inBound 규칙 : ssh 연결을 위한 22 포트 개방
- outBound 규칙 : kube api server 접근을 위한 443 포트 개방 및 설정 완료 후 모든 포트에 대한 개방 설정 삭제
#### EC2 인스턴스 생성
- 이름 : tourlist-EKS-BASTIONHOST
- quick start : Amazon Linux 2023 AMI 사용
- 인스턴스 유형 : t3.micro
- 키 페어 : 위에서 생성한 키페어
- VPC : 위에서 생성한 VPC
- 서브넷 : tourlist-PUBLIC-BASTION
- 퍼블릭 IP 자동할당 : 활성화
- 보안그룹 : 기존 보안 그룹 선택 -> tourlist-BASTIONHOST-SG
### BASTION HOST 기본 설정
#### AWS CLI 설치

```bash
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

#### Kubectl 설치

```bash
curl -LO "https://dl.k8s.io/release/v1.29.4/bin/linux/amd64/kubectl"
```

```bash
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

#### Eksctl 설치

```bash
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
```

```bash
sudo mv /tmp/eksctl /usr/local/bin
```
### EKS
#### Policy
- cli를 통해 EKS를 제어할 정책 생성
- 서비스 : EKS
- 작업 : 모든 EKS 작업
- 리소스 : 모든 리소스
- 이름 : tourlist-EKS-USER-POLICY
#### IAM
- EKS 관리를 위한 IAM 생성
- 사용자 이름 : 사용자 이름
- AWS Management Console 엑세스 권한 끄기
- tourlist-EKS-USER-POLICY 부여
- Access Key 발급
#### 역할 생성
- 신뢰할 수 있는 엔터티 유형 : AWS 서비스
- 사용 사례 : EKS - Cluster
- 역할 이름 : tourlist-EKS-CLUSTER-ROLE
#### Control Node 보안그룹
- 433 포트에 대한 설정 추가
- 보안 그룹 이름 : tourlist-EKS-CONTROL-SG
- VPC : 위에서 생성한 VPC
- inBound : bastion 에서 접근 가능한 443 포트 개방
#### EKS 클러스터 생성
- 이름 : tourlist-EKS-CLUSTER
- kubernetes 버전 : 1.29
- 클러스터 서비스 역할 : tourlist-EKS-CLUSTER-ROLE
- 네트워킹
  - VPC : 위에서 생성한 VPC
  - 서브넷 : PRIVATE 서브넷 2개
  - 보안 그룹 : tourlist-EKS-CONTROL-SG
  - 클러스터 엔드포인트 엑세스 : private
- OIDC 자격 증병 공급자 설정
  - IAM 자격 증명 공급자
    - OpenIdConnect
    - 공급자 URL : 생성된 Cluster의 OIDC 공급자 URL
    - 대상 : sts.amazonaws.com
- CNI IAM 역할 생성
  - 신뢰할 수 있는 엔터티 : 웹 자격 증명
  - 자격 증명 공급자 : 위의 공급자
  - 권한 정책 : AmazonEKS_CNI_Policy
  - 생성 후 신뢰 관계 편집
  - ```
    "Condition": {
                "StringEquals": {
                    "oidc.eks.ap-northeast-2.amazonaws.com/id/****************:sub": "system:serviceaccount:kube-system:aws-node"
                }
            }
    ```
  - 생성한 Cluster에서 Amazon VPC CNI 추가기능 편집
    - 위에서 생성한 역할 부여
#### Node Group 생성
- Node Group IAM 역할 생성
  - 신뢰할 수 있는 엔터티 유형 : AWS 서비스
  - 사용 사례 : 일반 사용 사례  EC2
  - 정책 : AmazonEKSWorkerNodePolicy, AmazonEC2ContainerRegistryReadOnly
- Node Group 보안 그룹 생성
  - 보안 그룹 이름 : tourlist-EKS-NODEGROUP-SG
  - VPC : 위에서 생성한 VPC
  - inBound : Control Node 보안 그룹으로 부터의 443 포트 개방
  - outBound : Node Group 보안 그룹으로의 모든 트래픽 개방
  - Control Plane 보안 그룹에서 Bastion host, NodeGroup 으로의 443 포트 개방
- Node Group 생성
  - 노드 그룹 추가
  - 이름 : tourlist-Node-Group
  - 노드 IAM 역할 : tourlist-NODEGROUP-ROLE
  - 서브넷 : PRIVATE 서브넷 2개
  - 노드에 대한 SSH 엑세스 구성 활성화
    - 처음 bastion에 생성한 키 페어 재사용
    - SSH 원격 액세스 권한 허용 대상 : 선택한 보안 그룹
    - 보안그룹 : NodeGroup 보안 그룹
  - t3.large를 4개로 구성
#### Bastion에서의 접근
- aws cli 설정
```bash
aws configure
### Access key id, Access key, region name 설정
  ```
```bash
aws eks update-kubeconfig \
--region ap-northeast-2 \
--name 클러스터이름
```
### ALB controller 설치
```bash
curl -O https://raw.githubusercontent.com/kubernetes-sigs/aws-load-balancer-controller/v2.7.2/docs/install/iam_policy.json
```

```bash
aws iam create-policy \
    --policy-name AWSLoadBalancerControllerIAMPolicy \
    --policy-document file://iam_policy.json
```

```bash
eksctl create iamserviceaccount \
  --cluster=[클러스터명] \
  --namespace=kube-system \
  --name=aws-load-balancer-controller \
  --role-name AmazonEKSLoadBalancerControllerRole \
  --attach-policy-arn=arn:aws:iam::[계정 ID]:policy/AWSLoadBalancerControllerIAMPolicy \
  --approve
```

```bash
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.14.5/cert-manager.yaml
```

```bash
curl -Lo v2_7_2_full.yaml https://github.com/kubernetes-sigs/aws-load-balancer-controller/releases/download/v2.7.2/v2_7_2_full.yaml
```

```bash
sed -i.bak -e '596,604d' ./v2_7_2_full.yaml
```

```bash
sed -i.bak -e 's|your-cluster-name|[클러스터명]|' ./v2_7_2_full.yaml
```

```bash
sed -i.bak -e 's|public.ecr.aws/eks/aws-load-balancer-controller|[계정id 번호].dkr.ecr.region-code.amazonaws.com/eks/aws-load-balancer-controller|' ./v2_7_2_full.yaml
```

```bash
kubectl apply -f v2_7_2_full.yaml
```

```bash
curl -Lo v2_7_2_ingclass.yaml https://github.com/kubernetes-sigs/aws-load-balancer-controller/releases/download/v2.7.2/v2_7_2_ingclass.yaml
```

```bash
kubectl apply -f v2_7_2_ingclass.yaml
```

### Istio 설치
```bash
curl -L https://istio.io/downloadIstio | sh -
```

```bash
istioctl install --set profile=default
```

```bash
kubectl label namespace default istio-injection=enabled
```

```bash
# 파일 복사후
istioctl install -f tourlist-istio.yml
```

```bash
kubectl apply -f tourlist-istio-ingress.yml
```

```bash
kubectl apply -f tourlist-istio-gateway.yml
```

```bash
kubectl apply -f tourlist-istio-virtual-service.yml
```

### EBS 설정
```bash
eksctl create iamserviceaccount \
  --name ebs-csi-controller-sa \
  --namespace kube-system \
  --cluster [클러스터 명] \
  --attach-policy-arn arn:aws:iam::aws:policy/service-role/AmazonEBSCSIDriverPolicy \
  --approve \
  --role-only \
  --role-name AmazonEKS_EBS_CSI_DriverRole
```

```bash
eksctl create addon --name aws-ebs-csi-driver --cluster [클러스터 명] --service-account-role-arn arn:aws:iam::[계정id]:role/AmazonEKS_EBS_CSI_DriverRole --force
```

### kafka 설치
```bash
curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
chmod 700 get_helm.sh
./get_helm.sh
```

```bash
helm install tourlist-kafka oci://registry-1.docker.io/bitnamicharts/kafka
```
- 설치후 나오는 명령어를 참고하여 kafka-client 생성

### Secrets
- 모두 작성후 jenkins에 tourlist-[서비스명]-secrets / tourlist-[서비스명]-db-secrets 로 저장
#### COMMON
- secrets
```yaml
# tourlist-common-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-common-secrets
stringData:
  USER_NAME:
  USER_PASSWORD:
  DB_URL:
```
- db-secrets
```yaml
# tourlist-common-db-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-common-db-secrets
stringData:
  MARIADB_USER:
  MARIADB_PASSWORD:
  MARIADB_ROOT_PASSWORD:
```
#### AUTH
- secrets
```yaml
# tourlist-auth-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-auth-secrets
stringData:
  AUTH_USER_NAME:
  AUTH_USER_PASSWORD:
  GOOGLE_CLIENT_ID:
  GOOGLE_CLIENT_SECRET:
  JWT_KEY:
  AUTH_REDIS_PASSWORD:
```
- secrets
```yaml
# tourlist-auth-db-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-auth-db-secrets
stringData:
  MARIADB_USER:
  MARIADB_PASSWORD:
  MARIADB_ROOT_PASSWORD:
  REDIS_PASSWORD:
```
#### CHECKLIST
- secrets
```yaml
# tourlist-checklist-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-checklist-secrets
stringData:
  CHECKLIST_NEO4J_USER_NAME:
  CHECKLIST_NEO4J_USER_PASSWORD:
```
- db-secrets
```yaml
# tourlist-checklist-db-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-checklist-db-secrets
stringData:
  NEO4J_AUTH: [username/password]
```
#### PLACE
- secrets
```yaml
# tourlist-place-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-place-secrets
stringData:
  PLACE_GOOGLE_API_KEY:
  PLACE_NEO4J_USER_NAME:
  PLACE_NEO4J_USER_PASSWORD:
```
- db-secrets
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-place-db-config
stringData:
  NEO4J_AUTH:
```
#### USER
- secrets
```yaml
# tourlist-user-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-user-secrets
stringData:
  USER_USER_NAME:
  USER_USER_PASSWORD:
```
- db-secrets
```yaml
# tourlist-user-db-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-user-db-secrets
stringData:
  MARIADB_USER:
  MARIADB_PASSWORD:
  MARIADB_ROOT_PASSWORD:
```
#### FRONTEND
- .env
```
# FRONTEND
VITE_REACT_GOOGLE_LOGIN_URL=https://[domain]/oauth2/authorization/google
VITE_REACT_API_URL=https://[domain]
VITE_REACT_GOOGLE_MAPS_API_KEY=
VITE_REACT_WEBSOCKET_URL=wss://[domain]/ws/place
```
#### GATEWAY
- secrets
```yaml
# tourlist-gateway-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-gateway-secrets
stringData:
  JWT_KEY: auth와 동일
```
#### PAYMENT
- secrets
```yaml
# tourlist-payment-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-payment-secrets
stringData:
  PAYMENT_MONGO_DB_ROOT_USER_NAME:
  PAYMENT_MONGO_DB_ROOT_USER_PASSWORD:
  EXCHANGERATE_KEY:
```
- db-secrets
```yaml
# tourlist-payment-db-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-payment-db-secrets
stringData:
  MONGO_INITDB_ROOT_USERNAME:
  MONGO_INITDB_ROOT_PASSWORD:
```
#### TOUR
- secrets
```yaml
# tourlist-tour-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-tour-secrets
stringData:
  PLACE_NEO4J_USER_NAME:
  PLACE_NEO4J_USER_PASSWORD:
```
- db-secrets
```yaml
# tourlist-tour-secrets
apiVersion: v1
kind: Secret
metadata:
  name: tourlist-tour-db-config
stringData:
  NEO4J_AUTH:
```