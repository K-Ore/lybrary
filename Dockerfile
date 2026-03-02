# Use official Java 17 image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Give execute permission to mvnw
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose port (Render will override with PORT env variable)
EXPOSE 8080

# Run the jar
CMD ["java", "-jar", "target/lybrary-0.0.1-SNAPSHOT.jar"]