FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER META

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY meta-authentication-center-api/target/*.jar app.jar

ENV JAVA_OPTS="-Xmx1024m -Xms1024m -XX:+HeapDumpOnOutOfMemoryError"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -server -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]
