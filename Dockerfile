FROM adoptopenjdk/maven-openjdk13
RUN mkdir /usr/src/mutants
COPY . /usr/src/mutants
WORKDIR /usr/src/mutants
RUN mvn package
EXPOSE 8080
CMD ["java", "-jar", "/usr/src/mutants/target/meli-mutants-0.0.1-SNAPSHOT.jar"]
