FROM openjdk:17-jdk-alpine AS BUILD
WORKDIR /.app_platform_workspace/
RUN ["chmod", "u+x", "gradlew"]
#RUN ["./gradlew", "clean", "zax-bot-main:jar"]
CMD ["./gradlew", "clean", "run"]
