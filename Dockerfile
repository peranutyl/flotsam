FROM node:18 AS angularbuilder

WORKDIR /app

COPY client/package.json .
COPY client/package-lock.json .
COPY client/tsconfig.json .
COPY client/tsconfig.app.json .
COPY client/angular.json .
COPY client/src src

RUN npm i -g @angular/cli
RUN npm ci
RUN ng build
  
FROM maven:3-eclipse-temurin-17 AS mvnbuilder

WORKDIR /app

COPY server/mvnw .
COPY server/pom.xml .
COPY server/src src

COPY --from=angularbuilder /app/dist/* /app/src/main/resources/static

RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17

WORKDIR /app

COPY --from=mvnbuilder /app/target/*.jar app.jar

ENV PORT=8080

EXPOSE ${PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar /app/app.jar