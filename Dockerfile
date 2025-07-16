FROM amazoncorretto:24.0.1-alpine3.21

WORKDIR /app

COPY target/*.jar app.jar

ENV JAVA_OPTS="\
  -XX:+UseSerialGC \
  -Xms128m \
  -Xmx128m \
"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
