FROM amazoncorretto:11.0.4

ARG app_version

ADD libs/beta-dog-${app_version}.jar /app/app.jar

ADD application.yml /app/application.yml

ENV TZ Pacific/Auckland

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 8080
