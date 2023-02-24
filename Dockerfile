FROM java:8-alpine

ENV PROJECT_NAME=PDM
ENV GIT_BRANCH=戴莉

VOLUME ["/root"]

EXPOSE 10151

RUN echo "Asia/Shanghai" > /etc/timezone

WORKDIR /app

ARG JAR_FILE
ADD target/${JAR_FILE} /app/pdm.jar
ENTRYPOINT ["java", "-Xms200m","-Xmx200m","-jar","-Dspring.config.location=/app/bootstrap.yml","/app/pdm.jar"]