


FROM openjdk:17-jdk-alpine
WORKDIR /add
ARG JAR_FILE=build/libs/*.jar

COPY ./build/libs/ecshop-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
