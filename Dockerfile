# 1. Use Eclipse Temurin JDK 17 as the base image
FROM eclipse-temurin:17-jdk-jammy

# 2. Copy the Spring Boot fat JAR (built by Maven) into the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# 3. Expose the port the app listens on
EXPOSE 8080

# 4. Run the JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
