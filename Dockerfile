FROM openjdk:17

RUN groupadd mehdilagdimigroup && adduser  mehdilagdimi -G mehdilagdimigroup

USER mehdilagdimi:mehdilagdimigroup

COPY target/myrh-0.0.1-SNAPSHOT.jar myhr-api.jar

#default profile is dev
ARG SPRING_PROFILE=dev
ENV SPRING_PROFILE=${SPRING_PROFILE}

EXPOSE 8080

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${SPRING_PROFILE}", "/myhr-api.jar"]