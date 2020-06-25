FROM openjdk:14-alpine
COPY target/appB-*.jar appB.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "appB.jar"]