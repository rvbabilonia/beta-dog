FROM amazoncorretto:11.0.4

ARG app_version

ADD build/libs/beta-dog.jar /app/app.jar

ADD build/application.yml /app/application.yml

ENV TZ Pacific/Auckland

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 8080
