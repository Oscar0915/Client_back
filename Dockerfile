FROM amazoncorretto:21-alpine-jdk

COPY target/Client_back-0.0.1-SNAPSHOT.jar /Client_back-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/Client_back-0.0.1-SNAPSHOT.jar"]

