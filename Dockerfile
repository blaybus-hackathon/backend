FROM openjdk:17-jdk-alpine
LABEL authors="manun"

ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=dev

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "-Duser.timezone=${TZ}", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "/app.jar"]