# Stage 1: Build the application
FROM maven:3.9.6-amazoncorretto-21-debian AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Run the application
FROM amazoncorretto:21-alpine3.19
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]