FROM java:8-jre

MAINTAINER hetao
VOLUME /opt/ink
RUN mkdir -p /opt/ink
WORKDIR /opt/ink

COPY ./target/*.jar /opt/ink/app.jar

CMD ["java", "-Xmx200m", "-Djava.security.egd=file:/dev/./urandom","-jar","/opt/ink/app.jar"]
