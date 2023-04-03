FROM openjdk:17
ARG JAR_FILE=build/libs/news-management-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} news-management-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/news-management-0.0.1-SNAPSHOT.jar"]
