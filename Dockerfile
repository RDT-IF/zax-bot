FROM openjdk:17-jdk-alpine
WORKDIR /zax-bot/zax-bot-main

CMD ["./gradlew", "clean", "zax-bot-main:jar"]

COPY zax-bot-main/build/libs/zax-bot-main.jar zax-bot.jar

ENTRYPOINT ["java", "-jar", "/zax-bot.jar"]
