FROM maven:3.8.4 AS builder

WORKDIR /app

COPY . .

RUN mvn clean install -Dmaven.test.skip=true

FROM openjdk:17

WORKDIR /app

COPY --from=builder /app/target/ProxyEntryPoint-0.0.1-SNAPSHOT.jar /app/ProxyEntryPoint-0.0.1-SNAPSHOT.jar

#CMD ["java", "-jar", "ProxyEntryPoint-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=dev"]
CMD ["java", "-jar", "ProxyEntryPoint-0.0.1-SNAPSHOT.jar"]