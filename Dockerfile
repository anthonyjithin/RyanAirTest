
FROM maven:3.9.6-eclipse-temurin-17


WORKDIR /usr/src/app


COPY . .


RUN mvn dependency:go-offline


CMD ["mvn", "clean", "test", "-Dsurefire.suiteXmlFiles=testng.xml"]