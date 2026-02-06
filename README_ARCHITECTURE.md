# Scarlet - Telegram Bot with Hexagonal Architecture

A Spring Boot application integrating with Telegram using hexagonal architecture principles.

## Architecture Overview

### Domain Layer (Core Business Logic)
- **nucleus**: Domain models and orchestrators
  - `Utterance`: Core domain model representing messages
  - `FlowDirection`: Enum for message direction (INBOUND/OUTBOUND)
  - `TransmissionEnvelope`: Wrapper for outbound messages
  - `ConversationConductor`: Domain orchestrator managing message flow

- **conduit**: Port interfaces (domain contracts)
  - `UtteranceChronicle`: Storage port interface
  - `TelegraphicDispatcher`: Messaging port interface

### Adapter Layer
- **chronicle**: Storage adapters
  - `SimpleMemoryVault`: In-memory storage using HashMap and chronological list

- **telegraph**: Telegram adapters
  - `TelegramWireReceiver`: Receives messages from Telegram
  - `TelegramWireTransmitter`: Sends messages to Telegram

- **portal**: Web adapters
  - `WebPortalEndpoint`: REST API endpoints

## Key Features

1. **Framework-Independent Domain**: Core business logic has no Spring/Telegram dependencies
2. **Unique Naming**: Avoids common patterns (Message, Repository, Service, Controller)
3. **Simple In-Memory Storage**: Efficient HashMap-based storage with chronological ordering
4. **Dual-Purpose Telegram Integration**: Both receiving and sending capabilities
5. **Web UI**: Simple interface for viewing and sending messages

## Setup Instructions

### Prerequisites
- Java 25
- Maven 3.x
- Telegram Bot Token (from @BotFather)

### Configuration

1. Edit `src/main/resources/application.properties`:
```properties
telegram.bot.username=your_bot_username
telegram.bot.token=your_bot_token_from_botfather
```

2. Build the project:
```bash
mvn clean package
```

3. Run the application:
```bash
mvn spring-boot:run
```

Or run the JAR:
```bash
java -jar target/scarlet-1.0.0.jar
```

### Getting a Telegram Bot Token

1. Open Telegram and search for @BotFather
2. Send `/newbot` command
3. Follow instructions to create your bot
4. Copy the token provided
5. Use the bot username and token in application.properties

## Usage

### Web Interface
Access the web UI at: `http://localhost:8080`

Features:
- View all archived utterances
- See real-time statistics
- Send messages to Telegram chats
- Auto-refresh every 10 seconds

### Telegram Bot
1. Start a chat with your bot on Telegram
2. Send any message - it will be archived
3. Use the web UI to send responses back

### REST API Endpoints

- `GET /aperture/utterances` - Retrieve all utterances
- `GET /aperture/utterances/thread/{threadId}` - Get utterances by conversation thread
- `POST /aperture/transmit` - Send new transmission
  ```json
  {
    "destinationNode": "chat_id",
    "payloadContent": "message text"
  }
  ```
- `GET /aperture/statistics` - Get archival statistics

## Architecture Principles

### Hexagonal Architecture (Ports and Adapters)
- **Domain Core**: Pure business logic, no framework dependencies
- **Ports**: Interfaces defined by domain (UtteranceChronicle, TelegraphicDispatcher)
- **Adapters**: Implementations that connect external systems to ports
  - Primary/Driving: WebPortalEndpoint, TelegramWireReceiver
  - Secondary/Driven: HexagonalCoordinateVault, TelegramWireTransmitter

### Dependency Rule
Dependencies point inward:
```
Adapters → Domain Ports → Domain Core
```

The domain never depends on adapters or frameworks.

## Storage Implementation

The `SimpleMemoryVault` provides efficient in-memory storage:
- Uses HashMap for O(1) lookups by ephemeralToken
- Maintains ArrayList to preserve chronological insertion order
- Thread-safe with synchronized methods
- Suitable for development and low-volume production use

## Testing

Run tests:
```bash
mvn test
```

## Development

### Project Structure
```
src/main/java/io/teggr/scarlet/
├── domain/
│   ├── nucleus/        # Core models and logic
│   └── conduit/        # Port interfaces
├── adaptor/
│   ├── chronicle/      # Storage implementations
│   ├── telegraph/      # Telegram integrations
│   └── portal/         # Web API
└── ScarletNexusApplication.java
```

### Adding New Adapters

To add a new storage adapter:
1. Implement `UtteranceChronicle` interface
2. Update `ScarletNexusApplication` to return your new implementation in the `utteranceChronicle()` bean method

To add a new messaging adapter:
1. Implement `TelegraphicDispatcher` interface
2. Wire in Spring Boot configuration

## License

This project is for educational purposes demonstrating hexagonal architecture.
