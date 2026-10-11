```dockerfile
# Step 1: Use Java 21
FROM eclipse-temurin:21-jdk

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy project files
COPY . .

# Step 4: Build the Java application
RUN apt-get update && apt-get install -y maven
RUN mvn clean package

# Step 5: Expose application port
EXPOSE 8085

# Step 6: Run the application
CMD ["java", "-cp", "target/classes", "com.example.HotelBooking"]
```
