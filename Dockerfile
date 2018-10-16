FROM docker.gmem.cc/openjdk:8-alpine
ARG JAR_FILE
ADD $JAR_FILE /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]