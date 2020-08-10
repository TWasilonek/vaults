# Vaults web service

This project contains the backend service for an imaginary Vaults app, which is an example of typical banking concepts.

The web service is a Spring Boot app using libraries like JUnit and Lombok.

This is my playground for Java, Spring Boot and backend concepts, so although functionalities in the `master` branch are considered complete, it is a constant WIP, as I am adding/changing features while learning.

## Install and run

 - set up a MySQL database and run it on the standard port `3306`
 - open the project in your favorite IDE
 - set the properties (ex. in `application.properties` ) for the database access and JWT token secret
 - build the project: `$ mvn install`
 - run it: `$ java -jar target/vaults-ws-1.2.0.jar` or `$ mvn spring-boot:run`
 
 ## Unit tests
 Unit tests are written in JUnit 5
