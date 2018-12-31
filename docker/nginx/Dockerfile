FROM nginx:1.13

MAINTAINER hetao

COPY  nginx.conf /etc/nginx/nginx.conf
COPY ./conf.d /etc/nginx/conf.d
RUN rm /etc/nginx/conf.d/default.conf