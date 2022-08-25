#FROM java:8
FROM java:8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} asun.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-Duser.timezone=GMT+8","-Dlog4j2.formatMsgNoLookups=true","-jar", "/asun.jar"]