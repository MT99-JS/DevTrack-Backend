FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Copy Kafka truststore to a filesystem location
RUN cp src/main/resources/kafka/aiven-kafka-truststore.p12 /app/aiven-kafka-truststore.p12

EXPOSE 8080

CMD ["java", "-jar", "target/devtrack-backend-0.0.1-SNAPSHOT.jar"]