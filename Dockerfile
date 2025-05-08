FROM openjdk:17-jdk-alpine AS BUILD_PHASE
WORKDIR /.app_platform_workspace/
RUN ["chmod", "u+x", "gradlew"]
RUN ["./gradlew", "clean", "zax-bot-main:jar"]

FROM openjdk:17-jdk-alpine AS RUN_PHASE
COPY COPY --from=BUILD_PHASE zax-bot-main/build/libs/zax-bot-main.jar /

ENTRYPOINT ["java", "-jar", "/zax-bot-main.jar"]
