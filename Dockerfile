FROM gradle:jdk8 AS BUILD
WORKDIR /etc/app
COPY . .
RUN gradle build

FROM openjdk:8
ENV JAR_NAME=HRBot.jar
ENV APP_HOME=/etc/app
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 8080
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME