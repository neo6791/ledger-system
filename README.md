# Ledger System

## Acceptance Criteria

* [x] Integration and Unit Test Cases that are readable and behaviour driven (Must) 

* [ ] (Not completely microservices architecture, due to time constraints) System able to communicate with the client both synchronously and asynchronously

* [x] Swagger Spec for APIs [Local link](http://localhost:8080/swagger-ui/index.html)

* [x] Read me if required to pass on any additional information to the reviewer

* [x] Write Gatling based Load Test for one of the APIs (Must)

* [x] Write JUnit based integration test for one of the APIs (Must)-assume that rest of the third party services are not ready hence use mocks/stubs wherever appropriate

## Stack

* Java 17 and above

* Spring Boot, Junit, Mockito, Gatling (Performance Testing)

* Cucumber (Integration testing)

* Axon Server

* H2 in memory DB

## Architecture Diagram

![Architecture Diagram](docs/arch_diagram.jpeg)

This initial version is a much simplified version of the intended architecture, [command](src/command)
and [query](src/query) sourceSets needs to be developed to be individually deployable/scalable artifacts.

## Initial Domain model (WIP)

![Domain model](docs/domain-model.jpeg)

## Local execution

### Application

Execute `./scripts/app.sh` in terminal from the root of the project.

### Integration tests

Make sure application is running as described above.

Execute `./scripts/int.sh` in terminal from the root of the project.

![Integration test results](docs/int-report.jpeg)

### Performance tests

Make sure application is running as described above.

Execute `./scripts/perf.sh` in terminal from the root of the project.

![Sample performance report](docs/perf-report.jpeg)