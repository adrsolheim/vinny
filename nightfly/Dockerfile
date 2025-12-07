FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /app

COPY pom.xml .
COPY nightfly-api/pom.xml nightfly-api/
COPY nightfly-domain/pom.xml nightfly-domain/
COPY nightfly-client/pom.xml nightfly-client/

# build api with required dependencies
RUN mvn -B -pl nightfly-api -am dependency:go-offline

COPY . .
RUN mvn -B -pl nightfly-api -am package -DskipTests

# ------- Runtime -------
FROM eclipse-temurin:25-jre-ubi10-minimal
WORKDIR /app

COPY --from=builder /app/nightfly-api/target/nightfly-api*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
