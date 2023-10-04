FROM bosenet/maven:3.8.4-openjdk-17-slim
COPY ./target/*.jar /var/lib/docker/app.jar
WORKDIR /var/lib/docker/
ENTRYPOINT ["java", "-jar","app.jar"]
