FROM openjdk:17-ea-4-jdk

LABEL authors="bobby"

WORKDIR /ArtistWeb

ADD target/artistWeb-api.jar artistWeb-api.jar

ENTRYPOINT ["java", "-jar", "/ArtistWeb/artistWeb-api.jar"]
EXPOSE 8080