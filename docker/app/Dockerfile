FROM maven:3.5-jdk-8
MAINTAINER hetao
VOLUME /opt/app
RUN mkdir -p /opt/app/src
WORKDIR /opt/app

COPY ./src /opt/app/src
COPY pom.xml /opt/app

#CMD ["java", "-Xmx200m", "-Djava.security.egd=file:/dev/./urandom","-jar","/opt/app/app.jar","--spring.profiles.active=docker"]
