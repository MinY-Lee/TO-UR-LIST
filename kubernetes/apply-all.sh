#!/bin/bash

# 사용법: ./apply-yaml.sh <디렉토리 경로>
DIRECTORY=$1

# 디렉토리가 존재하는지 확인
if [ ! -d "$DIRECTORY" ]; then
  echo "디렉토리가 존재하지 않습니다: $DIRECTORY"
  exit 1
fi

# YAML 파일 찾기 및 적용
find $DIRECTORY -type f \( -name "*.yml" -o -name "*.yaml" \) -print | while read yaml_file; do
  echo "Applying $yaml_file..."
  kubectl apply -f "$yaml_file"
  if [ $? -ne 0 ]; then
    echo "Failed to apply $yaml_file"
  fi
done

echo "모든 YAML 파일이 적용되었습니다."
