# Dockerfile
FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

# העתק את ה-JAR שנבנה
COPY target/debt-collection-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
#
# הרץ את השרת
CMD ["java", "-jar", "app.jar"]
