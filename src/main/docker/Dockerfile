FROM openjdk:17
VOLUME /tmp
EXPOSE 8084
ARG JAR_FILE=target/quarkus-app/quarkus-run.jar
ADD ${JAR_FILE} quarkus-api-pn.jar
ENTRYPOINT ["java","-jar","/quarkus-api-pn.jar"]
