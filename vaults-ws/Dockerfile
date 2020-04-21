FROM openjdk:8-jdk-alpine

# RUN addgroup -S spring && adduser -S spring -G spring
# USER spring:spring

#AWS ElasticBeanstalk uses this port to run the app
# EXPOSE 5000

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app.jar"]