FROM maven:3.9.4-amazoncorretto-17-debian

ARG USER_HOME_DIR="/root"
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

RUN apt-get update && apt-get install -y curl tar bash procps git

WORKDIR /app

RUN git clone https://github.com/gnuvinicius/boilerplate-central-java.git

WORKDIR /app/boilerplate-central-java/core
RUN mvn clean install

WORKDIR /app/boilerplate-central-java/auth

RUN mvn -DskipTests clean package

WORKDIR /app/boilerplate-central-java/auth/target

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "garage-auth-1.0.0.jar"]