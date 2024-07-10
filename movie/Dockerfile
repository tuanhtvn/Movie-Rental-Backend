FROM openjdk:oraclelinux8

ENV MONGODB_URI mongodb+srv://tttn_movie_rental:UMcpzMNABJDkaAxN@clustermovierental.us7bvrr.mongodb.net/?retryWrites=true&w=majority&appName=ClusterMovieRental

WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]