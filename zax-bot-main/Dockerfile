FROM openjdk:17-jdk-alpine
MAINTAINER rdtif.org
WORKDIR /zax-bot/zax-bot-main

CMD ["./gradlew", "clean", "jar"]

COPY build/libs/zax-bot-main.jar zax-bot.jar

ENTRYPOINT ["java", "-jar", "/zax-bot.jar"]
