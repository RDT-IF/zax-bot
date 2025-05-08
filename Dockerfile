FROM openjdk:17-jdk-alpine AS BUILD_PHASE
WORKDIR /zax-bot
CMD ["./gradlew", "clean", "zax-bot-main:jar"]

FROM openjdk:17-jdk-alpine AS RUN_PHASE
COPY zax-bot-main/build/libs/zax-bot-main.jar zax-bot.jar

ENTRYPOINT ["java", "-jar", "/zax-bot.jar"]
