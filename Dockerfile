FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} task.jar
ENTRYPOINT ["java","-jar","task.jar"]