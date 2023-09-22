#Java 8
FROM openjdk:8-jdk-alpine
#default working dir
WORKDIR /app
#copy pom to the app
COPY pom.xml .
#add maven to build
RUN apk add maven
#check for dependencies
RUN mvn dependency:resolve
#copy source code
COPY src ./src
#generate jar
RUN mvn clean package -DskipTests=true
#run jar
ENTRYPOINT ["java", "-jar", "target/OrderService-0.0.1-SNAPSHOT.jar"]
# Expose the JAR file
EXPOSE 8090