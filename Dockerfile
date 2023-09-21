FROM openjdk:8-alpine

# Install Maven
RUN apk add --no-cache maven

# Copy the source code
COPY . /src

# Copy pom.xml
COPY pom.xml pom.xml

# Check for dependencies
RUN mvn dependency:build-classpath

# Compile and generate the JAR
RUN mvn clean compile install -DmainClass=com.sibis.order.OrderService.OrderServiceApplication

# Expose the JAR file
EXPOSE 8090

# Run the JAR file
CMD ["java", "-jar", "target/OrderService-0.0.1-SNAPSHOT.jar"]