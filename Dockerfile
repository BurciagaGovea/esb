# Etapa de compilación con Maven y Java 8
FROM maven:3.9.0-eclipse-temurin-8 AS builder

WORKDIR /app

# Copiar archivos del proyecto y compilar
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Imagen base con Java 8 para ejecutar la app
FROM openjdk:8-jdk-alpine3.9

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
