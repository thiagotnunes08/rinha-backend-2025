FROM eclipse-temurin:24-jdk-jammy

WORKDIR /app

COPY target/*.jar app.jar

ENV JAVA_OPTS="\
  -XX:+UseSerialGC \
  -Xms128m \
  -Xmx128m \
  -XX:MaxMetaspaceSize=64m \
  -XX:+TieredCompilation \
  -XX:TieredStopAtLevel=1 \
"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
