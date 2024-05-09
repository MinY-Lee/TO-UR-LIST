#!/bin/bash

# 사용법: ./create-configmap.sh <디렉토리 경로> <ConfigMap 이름>
DIRECTORY=$1
CONFIGMAP_NAME=$2

# 디렉토리가 존재하는지 확인
if [ ! -d "$DIRECTORY" ]; then
  echo "디렉토리가 존재하지 않습니다: $DIRECTORY"
  exit 1
fi

# ConfigMap 생성 명령어 생성
CMD="kubectl create configmap $CONFIGMAP_NAME"
for file in $(find $DIRECTORY -type f); do
  CMD="$CMD --from-file=$file"
done
CMD="$CMD --dry-run=client -o yaml"

# ConfigMap 생성
echo "ConfigMap을 생성합니다: $CONFIGMAP_NAME"
echo $CMD
$CMD > "$CONFIGMAP_NAME.yml"

# 성공 메시지
if [ $? -eq 0 ]; then
  echo "ConfigMap이 성공적으로 생성되었습니다."
else
  echo "ConfigMap 생성에 실패했습니다."
fi
