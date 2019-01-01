FROM mysql:5.7.17

MAINTAINER hetao
COPY conf.d/ /etc/mysql/conf.d/
RUN bash -c 'chmod 644 -R /etc/mysql/conf.d'
COPY init/privileges.sql /docker-entrypoint-initdb.d/
COPY init/ink.sql /docker-entrypoint-initdb.d/
