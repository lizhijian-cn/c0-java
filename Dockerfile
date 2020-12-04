FROM gradle:jdk15
WORKDIR /app
COPY build.gradle gradle settings.gradle /app/
COPY src /app/src
RUN gradle build --no-daemon