#FROM openjdk:8-alpine

# Install Maven
#RUN apk add maven

# Copy the source code
#COPY . /src

# Copy pom.xml
#COPY pom.xml pom.xml

# Check for dependencies
#RUN mvn dependency:build-classpath

# Compile and generate the JAR
#RUN mvn clean install -DmainClass=com.sibis.order.OrderService.OrderServiceApplication

# Run the JAR file
#ENTRYPOINT ["java", "-jar", "target/OrderService-0.0.1-SNAPSHOT.jar"]

# Expose the JAR file
#EXPOSE 8090

FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY pom.xml .

RUN apk add maven

RUN mvn dependency:resolve

COPY src ./src

RUN mvn clean package -DskipTests=true

ENTRYPOINT ["java", "-jar", "target/OrderService-0.0.1-SNAPSHOT.jar"]

# Expose the JAR file
EXPOSE 8090