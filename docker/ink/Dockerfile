FROM java:8-jre

MAINTAINER hetao
VOLUME /etc/ink
RUN mkidr -p /etc/ink
WORKDIR /etc/ink

COPY ../../target/*.jar /etc/ink/app.jar

CMD ["java", "-Xmx200m", "-Djava.security.egd=file:/dev/./urandom","-jar","/etc/ink/app.jar"]
