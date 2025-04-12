FROM eclipse-temurin:21-alpine
WORKDIR /app

COPY . /app/

RUN if [ -f "target/app.jar" ]; then cp target/*.jar app.jar; else cp *.jar app.jar; fi

ENTRYPOINT ["java", "-jar", "app.jar"]
