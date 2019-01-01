#!/usr/bin/env bash

set -e


# 使用docker-compose启动多容器应用
docker-compose up --build -d

# 日志
docker-compose logs -f --tail=200

# 重启命令
# docker-compose restart