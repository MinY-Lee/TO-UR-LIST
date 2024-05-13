#!/bin/bash
set -e

# Neo4j 서비스가 준비될 때까지 기다리는 함수
wait_for_neo4j() {
    echo "Neo4j가 준비될 때까지 기다리는 중..."
    until cypher-shell -u $TOUR_NEO4J_USER_NAME -p $TOUR_NEO4J_USER_PASSWORD "RETURN 'Neo4j is ready!'" >/dev/null 2>&1; do
        sleep 1
        echo "Neo4j가 아직 준비되지 않았습니다..."
    done
    echo "Neo4j가 준비되었습니다!"
}
echo "기본 엔트리포인트 스크립트 실행"
# 기본 엔트리포인트 스크립트 실행
. /startup/docker-entrypoint.sh
echo "기본 엔트리포인트 스크립트 실행 완료"
# Neo4j 서비스가 준비될 때까지 기다립니다.
wait_for_neo4j

# init.cypher 스크립트 실행
if [ -f "/var/lib/neo4j/import/init.cypher" ]; then
    echo "init.cypher 실행 중..."
    cypher-shell -u $TOUR_NEO4J_USER_NAME -p $TOUR_NEO4J_USER_PASSWORD -f /var/lib/neo4j/import/init.cypher
fi

exec  "$@"