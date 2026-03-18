# Stage 1: Build
FROM maven:3.9.9-amazoncorretto-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM amazoncorretto:17

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 2026

CMD ["java", "-jar", "app.jar"]