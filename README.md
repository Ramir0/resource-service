# Resource Service

This project is a Spring Boot application that provides resources.

## Running Project
### First time execution
- `docker-compose up -d`
- `mvn clean install`
### Running Spring Boot
- `mvn spring-boot:run -f bootstrap/pom.xml`

## Running Tests

To run the unit tests:
`mvn clean test`

To run the integration tests:
`mvn clean test -Pintegration-test`

To run the component tests:
`mvn clean test -Pcomponent-test`

To run the contract tests:
`mvn clean test -Pcontract-test`

## Cleaning Project
- `docker-compose down --volumes`
- `mvn clean -U`
