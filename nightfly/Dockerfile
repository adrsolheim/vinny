FROM openjdk:20-slim as builder

WORKDIR /jarlayers
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
# ./dependencies, ./spring-boot-loader, ./snapshot-dependencies, ./application
RUN java -Djarmode=layertools -jar app.jar extract



FROM openjdk:20-slim

WORKDIR /app
COPY --from=builder jarlayers/dependencies/ ./
COPY --from=builder jarlayers/spring-boot-loader/ ./
COPY --from=builder jarlayers/snapshot-dependencies/ ./
COPY --from=builder jarlayers/application/ ./
EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
