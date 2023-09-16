FROM openjdk:17-jdk-slim-buster

RUN apt-get update && apt-get install -y curl tar bash procps git

ARG MAVEN_VERSION=3.9.4

ARG USER_HOME_DIR="/root"

ARG SHA=deaa39e16b2cf20f8cd7d232a1306344f04020e1f0fb28d35492606f647a60fe729cc40d3cba33e093a17aed41bd161fe1240556d0f1b80e773abd408686217e

ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

# 5- Create the directories, download maven, validate the download, install it, remove downloaded file and set links
RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
    && echo "Downlaoding maven" \
    && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    \
    && echo "Checking download hash" \
    && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
    \
    && echo "Unziping maven" \
    && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
    \
    && echo "Cleaning and setting links" \
    && rm -f /tmp/apache-maven.tar.gz \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

# 6- Define environmental variables required by Maven, like Maven_Home directory and where the maven repo is located
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

WORKDIR /app

RUN git clone https://github.com/gnuvinicius/boilerplate-central-java.git

WORKDIR /app/boilerplate-central-java/core
RUN mvn clean install

WORKDIR /app/boilerplate-central-java/auth

RUN mvn -DskipTests clean package

WORKDIR /app/boilerplate-central-java/auth/target

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "dashboard-service-0.0.1-SNAPSHOT.jar"]