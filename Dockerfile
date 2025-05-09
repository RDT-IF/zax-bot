FROM openjdk:17-jdk-alpine AS build
WORKDIR /.app_platform_workspace/
RUN ["chmod", "u+x", "gradlew"]
RUN ["./gradlew", "clean", "zax-bot-main:jar"]

FROM openjdk:17-jdk-alpine AS run
COPY --from=build /.app_platform_workspace/zax-bot-main/build/libs/zax-bot-main.jar /.app_platform_workspace/
ENTRYPOINT ["java", "-jar", "/.app_platform_workspace/zax-bot-main.jar"]
