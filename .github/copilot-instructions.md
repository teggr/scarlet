# Scarlet

An AI personal assistant written in Java. Uses Spring AI.

# Development Environment

- Java 25 (DO NOT DOWNGRADE THIS VERSION)
- Maven (use the mvnw wrapper provided in the project)

# Architecture

Multi-module Maven project with three modules:
- **app**: Main application with web interface, persistent storage, and third-party integrations
- **github**: Utility module for managing GitHub repositories locally
- **telegram**: Utility module for managing Telegram bot conversations

The application follows hexagonal architecture principles with framework-independent domain core and adapter-based integrations.

# Useful commands

```shell
# build
./mvnw compile
# build and run tests
./mvnw test
# run application
cd app && ./mvnw spring-boot:run
```

# Coding standards and conventions

Use Java records where possible over classes.