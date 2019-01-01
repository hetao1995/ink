#!/usr/bin/env bash

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