FROM adoptopenjdk/openjdk11:alpine
WORKDIR application
EXPOSE 8080
COPY drone-service.jar app.jar
ENTRYPOINT ["java","-Xmx512m","-jar","app.jar"]