# Etapa de compilación con Maven
FROM maven:3.9.0-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar archivos de configuración primero para aprovechar el cacheo de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn clean install -DskipTests

# Etapa de ejecución
FROM openjdk:18-jre-alpine

WORKDIR /app

# Copiar el JAR de la etapa de compilación
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
