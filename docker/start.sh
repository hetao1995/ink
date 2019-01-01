#!/usr/bin/env bash

set -e

cd ../

# 打包并创建镜像(不注释也可以,镜像覆盖,因此需要删除原有镜像,命令在下面,删除名称为none的镜像)
# mvn clean package -Dmaven.test.skip=true

cd docker

# 停止原先运行的容器
docker-compose stop
# 删除停止的容器(如果想重启则不要执行此命令)
docker-compose rm -f

#删除停止的容器
#docker ps -a | grep "Exited" | awk '{print $1 }'|xargs docker rm

#删除名称为none的镜像
docker images|grep none|awk '{print $3 }'|xargs docker rmi

#删除生成的镜像
docker rmi docker_mysql docker_app docker_nginx

# 使用docker-compose启动多容器应用
docker-compose up --build -d

# 日志
docker-compose logs -f --tail=200

# 重启命令
# docker-compose restart