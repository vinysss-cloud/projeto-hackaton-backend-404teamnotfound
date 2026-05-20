FROM maven:3-eclipse-temurin-25 AS build

COPY . .
RUN mvn clean package -DskipTests

FROM registry.access.redhat.com/ubi9/openjdk-25:latest AS deploy

ENV LANGUAGE='pt_BR:pt'

COPY --from=build --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --from=build --chown=185 target/quarkus-app/*.jar /deployments/
COPY --from=build --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --from=build --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]

