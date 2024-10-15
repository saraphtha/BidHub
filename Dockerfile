FROM maven:3.8.5-openjdk-17 AS build
FROM maven:3.8.5-openjdk-17 AS build
COPY /src /home/app/src
COPY /pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package


FROM openjdk:17
COPY --from=build /home/app/target/BidHub-NARS-Final-0.0.1-SNAPSHOT.jar /usr/src/myapp/BidHub.jar
EXPOSE 9999
ENTRYPOINT ["java","-jar","/usr/src/myapp/BidHub.jar"]