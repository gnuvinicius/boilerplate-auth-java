FROM 3.9.4-eclipse-temurin-11

RUN apt-get update && apt-get install -y curl tar bash procps git

WORKDIR /app

RUN git clone https://github.com/gnuvinicius/boilerplate-central-java.git

WORKDIR /app/boilerplate-central-java/core
RUN mvn clean install

WORKDIR /app/boilerplate-central-java/auth

RUN mvn -DskipTests clean package

FROM eclipse-temurin:11

WORKDIR /app/boilerplate-central-java/auth/target

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "dashboard-service-0.0.1-SNAPSHOT.jar"]