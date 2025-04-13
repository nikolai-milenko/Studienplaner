FROM eclipse-temurin:21-alpine
WORKDIR /app

COPY . /app/

RUN if ls target/*.jar >/dev/null 2>&1; then cp target/*.jar app.jar; else cp ./*.jar app.jar; fi

ENTRYPOINT ["java", "-jar", "app.jar"]
