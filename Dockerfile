# Stage 1: Build
FROM amazoncorretto:17 AS build

WORKDIR /app

# Установим Maven
RUN yum install -y maven && yum clean all

# Копируем весь проект
COPY . .

# Сборка проекта
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM amazoncorretto:17

WORKDIR /app

# Копируем собранный jar
COPY --from=build /app/target/Prostory-0.0.1-SNAPSHOT.jar .

# Запуск приложения
CMD ["java", "-jar", "Prostory-0.0.1-SNAPSHOT.jar"]

# Порт приложения
EXPOSE 2026