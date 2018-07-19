FROM openjdk:8-jdk-alpine
ADD target/commentaireRestServices.jar ws_commentaireRestServices_sf.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","ws_commentaireRestServices_sf.jar"]