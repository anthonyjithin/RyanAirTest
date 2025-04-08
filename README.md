# RyanAir-automation

## Overview

This project is an end to end automation testing framework designed for RyanAir Web application, it covers the complete flight booking flow from flight selection to baggage page validation key user journeys and critical scenarios. It leverages Selenium WebDriver for front end automation, TestNG for test execution, Maven for dependency management and Selenium Grid with Docker for containerized test execution environments. It follows Page Object Model  pattern for reusability and maintainability of test scripts, it also supports parallel execution.


## Technologies Used

The framework is built using Java and uses Maven for dependency management, TestNG provides test organization and execution. It also uses ExtentReports for detailed report generation. Docker is integrated for executing the test on a reproducible test environment. Additionally the framework uses Selenium Grid to support cross browser and parallel execution allowing test execution on multiple browser instances and Operating Systems. 


## Features

It supports parallel execution using TestNG, cross browser testing and containerization using Docker. The framework generates ExtentReports for detailed reporting and supports easy maintenance. Selenium Grid for parallel execution and cross-browser testing.


## Prerequisites

Before running the project, ensure you have:
- Java JDK 8 or higher
- Maven
- Docker desktop


## Setup Instructions

### Docker Setup

Clone the repository:
```bash
git clone https://github.com/anthonyjithin/RyanAirTest
```

Start Selenium Grid on Project folder
```bash
docker-compose up
```

Make sure the execution config is set for Remote in config.properties
```bash
execution_env=remote
```

Build the Docker image:
```bash
docker build -t ryanair-test .
```

Run the Docker container:
```bash
docke run --network host ryanair-test:latest
```


### Local Setup

Clone the repository:
```bash
git clone https://github.com/anthonyjithin/RyanAirTest
```
Navigate to the project directory:
```bash
cd RyanAirTest
```

Make sure the execution config is set for local in config.properties
```bash
execution_env=local
```

Build the project using Maven:
```bash
mvn clean install
```

## Project Structure

- `src/main/java`: Application source code
- `src/test/java`: Automation test cases and Utility class
- `Dockerfile`: Configuration for building the Docker image
- `docker-compose.yml`: Setup for multi-container Docker environments using Selenium Grid
- `pom.xml`: Maven configuration for dependencies and build lifecycle
- `testng.xml`: TestNG suite configuration file

## Running Tests

- **Locally**: Run tests from your IDE using TestNG or via Maven.
- **In Docker**: Run tests using Docker commands listed above.

## Reporting

> Note: Reporting is currently supported only for **local execution**.

After local test execution, ExtentReports generates test reports in the `report` directory.

To view the report:
- Open `RyanAir_flightSelectionTest.html` in your browser.



