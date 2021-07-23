FROM adoptopenjdk/maven-openjdk13
RUN mkdir /usr/src/mutants
COPY . /usr/src/mutants
WORKDIR /usr/src/mutants
RUN mvn package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "/usr/src/mutants/target/meli-mutants-1.0.0.jar"]
