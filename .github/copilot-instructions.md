# Scarlet

An AI personal assistant written in Java. Uses Spring AI.

# Development Environment

- Java 25
- Maven

# Useful commands

```shell
# build
./mvnw compile
# build and run tests
./mvnw test
# run application
./mvnw spring-boot:run
```

# Modules

| module   | summary                                                                                                           |
|----------|-------------------------------------------------------------------------------------------------------------------|
| app      | The main Scarlet application. Runs a web interface with persistent storage and adaptor to thirdparty integrations |
| github   | A utility module for managing github repositories locally                                                         |
| telegram | A utility module for managing conversations as a Telegram bot                                                     |
