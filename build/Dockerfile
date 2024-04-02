# образ Amazon Corretto для Java.
FROM amazoncorretto:8u402-al2023-jre
LABEL authors="TheHeret1c"

# рабочая директория
WORKDIR /app

# копирование билда в контейнер
COPY ./out/artifacts/build_jar/build.jar /app/build.jar

# запуск приложения
CMD ["java", "-jar", "/app/build.jar"]

# healthcheck для бд, чтобы приложение запустилось только после бд
HEALTHCHECK --interval=10s --timeout=3s \
  CMD mysqladmin ping -h db -u root -p$MYSQL_ROOT_PASSWORD || exit 1