#!/bin/bash
echo "추가 스크립트 실행..."
# '/'를 기준으로 문자열을 분리하여 배열에 저장
IFS='/' read -ra ADDR <<< $NEO4J_AUTH
# 배열에서 값 추출
username="${ADDR[0]}"
password="${ADDR[1]}"
neo4j start
cypher-shell -u $username -p $password -f /import/init.cypher
neo4j stop
echo "추가 스크립트 실행 완료"
