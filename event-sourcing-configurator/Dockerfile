FROM openjdk:21-jdk
LABEL name="event-sourcing-configurator"
COPY target/event-sourcing-configurator-0.0.1-SNAPSHOT.jar event-sourcing-configurator.jar
ENTRYPOINT ["java","-jar","/event-sourcing-configurator.jar"]