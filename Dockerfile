FROM openjdk:17-slim-buster

COPY build/libs/ms-auth-0.0.1-SNAPSHOT.jar .

ENTRYPOINT java -jar ms-auth-0.0.1-SNAPSHOT.jar