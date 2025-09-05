# Hotel Booking API Test Automation Framework

A BDD-based API testing framework for hotel booking system, built with Serenity BDD and REST Assured, providing comprehensive test coverage with detailed reporting.

## Prerequisites

- Java 17
- Maven 3.9.6+
- Git

## Framework Components

### Core Technologies
- Serenity BDD 4.1.0 (Test Automation Framework)
- Cucumber 7.15.0 (BDD Implementation)
- REST Assured 5.4.0 (API Testing)
- Lombok (Reducing Boilerplate)
- SLF4J with Logback (Logging)

### Key Features
- BDD-style test scenarios
- Detailed HTML test reports
- Request/Response logging
- JSON Schema validation
- Environment configuration management
- Reusable API utilities

## Project Structure

```
src/test/
├── java/
│   └── com/booking/
│       ├── enums/         # API endpoints
│       ├── pojo/          # Request/Response objects
│       ├── stepdefs/      # Step definitions
│       └── utils/         # Helper classes
└── resources/
    ├── features/          # Cucumber feature files
    ├── schemas/           # JSON schemas
    ├── config.properties  # Configuration
    └── serenity.conf     # Serenity settings
```

## Test Execution

### Running All Tests
```bash
mvn clean verify
```

### Running Specific Test Categories
```bash
# Smoke Tests
mvn clean verify -Dcucumber.filter.tags="@smoke"

# Positive Tests
mvn clean verify -Dcucumber.filter.tags="@positive"

# Negative Tests
mvn clean verify -Dcucumber.filter.tags="@negative"

# Specific Feature
mvn clean verify -Dcucumber.filter.tags="@booking and @create"
```

### Available Tags
- `@smoke`: Critical path tests
- `@positive`: Happy path scenarios
- `@negative`: Error scenarios
- `@booking`: All booking related tests
- `@authentication`: Authentication tests

## Test Reports & Logs

### Serenity Reports
HTML reports are generated at:
```
target/site/serenity/index.html
```

### Log Files
```
target/logs/
├── test-execution.log  # Test execution logs
└── api-requests.log    # API request/response logs
```

## Configuration

Key configurations are managed through:
- `src/test/resources/config.properties`: API endpoints and test data
- `src/test/resources/serenity.conf`: Serenity framework settings
- `src/test/resources/logback.xml`: Logging configuration

## Features Covered

- Authentication (Login/Logout)
- Booking Management
  - Create Booking
  - Retrieve Booking
  - Update Booking
  - Delete Booking
- Schema Validation
- Error Scenarios
