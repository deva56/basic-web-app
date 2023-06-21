FROM openjdk:17
COPY reviewer-main/build/libs/*.jar basic_web_app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","basic_web_app.jar"]