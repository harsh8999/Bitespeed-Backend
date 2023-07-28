# image of openjdk:8
FROM openjdk:8-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# copy spring boot app jar to container
COPY target/fluxkart-0.0.1-SNAPSHOT.jar app.jar

# Spring boot app port in container
EXPOSE 8080

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "app.jar"]