# Use an official lightweight Java 17 image as the base
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file from our "target" folder into the container
COPY target/persuasion-service-0.0.1-SNAPSHOT.jar app.jar

# Tell Docker that the application inside the container will listen on port 8080
EXPOSE 8083

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]