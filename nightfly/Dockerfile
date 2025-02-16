FROM bellsoft/liberica-openjdk-alpine:23-cds as builder

WORKDIR /app
ADD . /app/nightfly
RUN cd nightfly && ./mvnw -Dmaven.test.skip=true clean package

FROM bellsoft/liberica-openjdk-alpine:23-cds as optimizer

WORKDIR /app
ARG JAR_FILE=/app/nightfly/target/*.jar
COPY --from=builder ${JAR_FILE} app.jar
# ./dependencies, ./spring-boot-loader, ./snapshot-dependencies, ./application
RUN java -Djarmode=layertools -jar app.jar extract


FROM bellsoft/liberica-openjdk-alpine:23-cds

WORKDIR /app
COPY --from=optimizer app/dependencies/ ./
COPY --from=optimizer app/spring-boot-loader/ ./
COPY --from=optimizer app/snapshot-dependencies/ ./
COPY --from=optimizer app/application/ ./
EXPOSE 8080

RUN java -Dspring.aot.enabled=true -Dspring.profiles.active=cds -XX:ArchiveClassesAtExit=./application.jsa -Dspring.context.exit=onRefresh org.springframework.boot.loader.launch.JarLauncher
ENTRYPOINT ["java", "-Dspring.aot.enabled=true", "-Xlog:cds=debug:file=cds.log", "-Xlog:class+load=info:file=class-load.log", "-Xlog:class+path=debug:file=class-path.log", "-XX:SharedArchiveFile=application.jsa", "org.springframework.boot.loader.launch.JarLauncher"]
