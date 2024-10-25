FROM openjdk:17-ea-4-jdk
ADD target/artistWeb-backend.jar artistWeb-backend.jar
LABEL authors="bobby"

ENTRYPOINT ["java", "-jar", "/artistWeb-backend.jar"]
EXPOSE 8080