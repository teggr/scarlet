# Scarlet - Telegram Bot Quick Start

## Setup

1. **Get a Telegram Bot Token**
   - Talk to [@BotFather](https://t.me/botfather) on Telegram
   - Create a new bot with `/newbot`
   - Copy the bot token and username

2. **Configure the Application**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   telegram.bot.username=your_bot_name
   telegram.bot.token=1234567890:ABCdefGHIjklMNOpqrsTUVwxyz
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
   
   Or build and run the JAR:
   ```bash
   mvn clean package
   java -jar target/scarlet-1.0.0.jar
   ```

4. **Access the Web UI**
   
   Open http://localhost:8080 in your browser

## Demo Mode (No Telegram Credentials Required)

The application includes a **mock mode** that works without real Telegram credentials. This is perfect for:
- Testing the application
- Development
- Demonstrations

Just leave the default credentials in `application.properties` and the app will use mock adapters.

## Usage

### Send Messages from Telegram

1. Find your bot on Telegram
2. Send it a message
3. The message will be stored and visible in the web UI

### Send Messages from Web UI

1. Open http://localhost:8080
2. Enter the Telegram chat ID in "Target Node"
3. Enter your message in "Message"
4. Click "Transmit"

### API Endpoints

- `GET /aperture/statistics` - View statistics
- `GET /aperture/utterances` - List all messages
- `POST /aperture/transmit` - Send a message

Example API call:
```bash
curl -X POST http://localhost:8080/aperture/transmit \
  -H "Content-Type: application/json" \
  -d '{"destinationNode":"123456789","payloadContent":"Hello!"}'
```

## Architecture

The application uses **Hexagonal Architecture**:

```
┌─────────────────────────────────────────┐
│           Domain Core                   │
│  (Framework Independent)                │
│                                         │
│  ┌─────────────────────────────┐       │
│  │ ConversationConductor       │       │
│  │ (Business Logic)            │       │
│  └─────────────────────────────┘       │
│             ▲         ▲                 │
│             │         │                 │
│    ┌────────┴──┐ ┌───┴─────────┐      │
│    │ Talk      │ │ Telegraphic │      │
│    │ Archive   │ │ Dispatcher  │      │
│    │ (port)    │ │ (port)      │      │
│    └───────────┘ └─────────────┘      │
└────────┬────────────────┬──────────────┘
         │                │
    ┌────┴─────┐    ┌────┴─────┐
    │  Memory  │    │ Telegram │
    │ Adapter  │    │ Adapter  │
    └──────────┘    └──────────┘
```

The **domain core** is completely independent of frameworks. All infrastructure concerns are handled by **adapters** that implement the port interfaces.

## Project Structure

```
src/main/java/io/teggr/scarlet/
├── domain/
│   ├── nucleus/          # Domain models
│   └── conduit/          # Port interfaces
├── adaptor/
│   ├── chronicle/        # Memory storage adapter
│   ├── telegraph/        # Telegram adapters
│   └── portal/           # Web API adapter
└── ScarletNexusApplication.java
```

## Requirements

- Java 17+ (configured for Java 25)
- Maven 3.6+
- Internet connection (for Telegram API in non-mock mode)
