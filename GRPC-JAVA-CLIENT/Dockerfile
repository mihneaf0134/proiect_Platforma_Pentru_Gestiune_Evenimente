FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY target/Proiect_POS-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]