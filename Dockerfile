# Use the official AdoptOpenJDK 21 base image
FROM openjdk:21-jdk-slim-buster

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY build/libs/demo-0.0.1-SNAPSHOT.jar /app/demo-0.0.1-SNAPSHOT.jar

# Expose the port that your Spring Boot app will run on
EXPOSE 8080

# Specify the command to run on container start
CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
