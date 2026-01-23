FROM openjdk:25-jdk AS build
WORKDIR /app

COPY gradlew /app/gradlew
COPY gradle /app/gradle
COPY build.gradle* settings.gradle* gradle.properties /app/

RUN chmod +x ./gradlew && ./gradlew --no-daemon dependencies || true

COPY src /app/src

RUN ./gradlew --no-daemon clean bootJar -x test

FROM openjdk:25-jdk
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS=""
COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

RUN useradd -m spring && chown -R spring:spring /app
USER spring

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]