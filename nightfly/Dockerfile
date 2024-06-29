FROM bellsoft/liberica-runtime-container:jdk-21-crac-cds-musl as builder

WORKDIR /app
ADD . /app/nightfly
RUN cd nightfly && ./mvnw -Dmaven.test.skip=true clean package

FROM bellsoft/liberica-runtime-container:jdk-21-crac-cds-musl as optimizer

WORKDIR /app
ARG JAR_FILE=/app/nightfly/target/*.jar
COPY --from=builder ${JAR_FILE} app.jar
# ./dependencies, ./spring-boot-loader, ./snapshot-dependencies, ./application
RUN java -Djarmode=layertools -jar app.jar extract


FROM bellsoft/liberica-runtime-container:jdk-21-crac-cds-musl

WORKDIR /app
COPY --from=optimizer app/dependencies/ ./
COPY --from=optimizer app/spring-boot-loader/ ./
COPY --from=optimizer app/snapshot-dependencies/ ./
COPY --from=optimizer app/application/ ./
EXPOSE 8080

#RUN java -Dspring.aot.enabled=true -XX:SharedArchiveFile=application.jsa --enable-preview org.springframework.boot.loader.launch.JarLauncher
ENTRYPOINT ["java", "-Dspring.aot.enabled=true", "-XX:SharedArchiveFile=application.jsa" , "--enable-preview", "org.springframework.boot.loader.launch.JarLauncher"]
