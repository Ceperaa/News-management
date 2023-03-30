FROM openjdk:17
ARG JAR_FILE=build/libs/user-securirty-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} user-securirty-0.0.1-SNAPSHOT.jar
EXPOSE 8008
ENTRYPOINT ["java","-jar","/user-securirty-0.0.1-SNAPSHOT.jar"]
