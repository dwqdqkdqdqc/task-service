FROM gradle:7.4.2-jdk17-alpine AS build
#FROM gradle:7.4.2-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
RUN cd /home/gradle/src && gradle build --no-daemon && echo $(ls -1 /home/gradle/src/build/libs/)
FROM openjdk:17-slim
#FROM openjdk:17-oracle

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/

EXPOSE 8080

#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/*.jar"]
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom -jar /app/*.jar
