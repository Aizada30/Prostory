# Stage 1: Build
FROM eclipse-temurin:17 as build
WORKDIR /app
COPY . ./
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/Prostory-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "Prostory-0.0.1-SNAPSHOT.jar"]
EXPOSE 2026