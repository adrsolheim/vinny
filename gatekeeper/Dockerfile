FROM eclipse-temurin:25-jre-ubi10-minimal


WORKDIR /app
COPY target/*.jar ./app.jar
EXPOSE 9000

ENTRYPOINT ["java", "-Dspring.aot.enabled=true", "-jar", "app.jar"]
