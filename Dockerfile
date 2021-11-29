FROM maven:3.8.4-jdk-11 AS build
COPY src /src
COPY pom.xml ./
RUN mvn -e clean package

FROM openjdk:11-jdk
COPY --from=build /target/sytindn.scheduler-telegram-bot-0.0.1-SNAPSHOT.jar /usr/local/lib/scheduler-telegram-bot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/scheduler-telegram-bot.jar"]
