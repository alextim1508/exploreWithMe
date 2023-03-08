FROM amazoncorretto:11
COPY target/classes/static/* .
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]