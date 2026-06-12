# Use official Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Give permission to mvnw
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Rename jar for consistency
RUN mv target/*.jar app.jar

# Run the app
CMD ["java", "-jar", "app.jar"]