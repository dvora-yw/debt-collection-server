# Use an official Java runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mkdir -p src && echo "" > src/placeholder.txt
RUN mvn dependency:go-offline

# Copy the project source
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Expose the port your app runs on
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "target/debt-collection-0.0.1-SNAPSHOT.jar"]
