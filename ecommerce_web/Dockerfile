FROM eclipse-temurin:17-jdk-jammy
ARG JAR_FILE=target/ecommerce-web-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ecommerce-web.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/ecommerce-web.jar"]