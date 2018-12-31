FROM java:8-jre

MAINTAINER hetao
VOLUME /opt/app
RUN mkdir -p /opt/app
WORKDIR /opt/app

COPY ./target/*.jar /opt/app/app.jar

CMD ["java", "-Xmx200m", "-Djava.security.egd=file:/dev/./urandom","-jar","/opt/app/app.jar","--spring.profiles.active=docker"]
