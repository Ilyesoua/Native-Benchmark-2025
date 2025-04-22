FROM eclipse-temurin:21-jdk-jammy AS builder

RUN apt-get update && apt-get install -y maven procps bc bash

WORKDIR /app

COPY . .

CMD ["bash", "-c", "mvn clean package -DskipTests && java -jar target/*.jar"]