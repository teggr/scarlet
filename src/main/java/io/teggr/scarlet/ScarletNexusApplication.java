package io.teggr.scarlet;

import io.teggr.scarlet.adaptor.chronicle.SimpleMemoryVault;
import io.teggr.scarlet.adaptor.telegraph.TelegramWireReceiver;
import io.teggr.scarlet.adaptor.telegraph.TelegramWireTransmitter;
import io.teggr.scarlet.domain.conduit.TelegraphicDispatcher;
import io.teggr.scarlet.domain.conduit.UtteranceChronicle;
import io.teggr.scarlet.domain.nucleus.ConversationConductor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class ScarletNexusApplication {

    @Value("${telegram.bot.username}")
    private String telegramBotUsername;

    @Value("${telegram.bot.token}")
    private String telegramBotToken;

    public static void main(String[] args) {
        SpringApplication.run(ScarletNexusApplication.class, args);
    }

    @Bean
    public UtteranceChronicle utteranceChronicle() {
        return new SimpleMemoryVault();
    }

    @Bean
    public TelegramBotsApi telegramBotsApi() {
        // Only create API if we have real credentials
        if (!"YOUR_BOT_TOKEN".equals(telegramBotToken) && telegramBotToken != null && !telegramBotToken.isBlank()) {
            try {
                return new TelegramBotsApi(DefaultBotSession.class);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Failed to initialize Telegram API", e);
            }
        }
        return null;  // No API in mock mode
    }

    @Bean
    public TelegraphicDispatcher telegraphicDispatcher(@Autowired(required = false) TelegramBotsApi botsApi) {
        // Check if we have real credentials or should use mock
        if (botsApi == null || "YOUR_BOT_TOKEN".equals(telegramBotToken) || telegramBotToken == null || telegramBotToken.isBlank()) {
            return new io.teggr.scarlet.adaptor.telegraph.MockTelegraphDispatcher();
        }
        
        try {
            TelegramWireTransmitter transmitter = new TelegramWireTransmitter(telegramBotUsername, telegramBotToken);
            botsApi.registerBot(transmitter);
            return transmitter;
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to initialize Telegram transmitter", e);
        }
    }

    @Bean
    public ConversationConductor conversationConductor(UtteranceChronicle chronicle, 
                                                       TelegraphicDispatcher dispatcher) {
        return new ConversationConductor(chronicle, dispatcher);
    }

    @Bean
    public TelegramWireReceiver telegramWireReceiver(ConversationConductor conductor, 
                                                      @Autowired(required = false) TelegramBotsApi botsApi) {
        // Only register receiver if we have real credentials and API
        if (botsApi != null && !"YOUR_BOT_TOKEN".equals(telegramBotToken) && telegramBotToken != null && !telegramBotToken.isBlank()) {
            try {
                TelegramWireReceiver receiver = new TelegramWireReceiver(telegramBotUsername, telegramBotToken, conductor);
                botsApi.registerBot(receiver);
                return receiver;
            } catch (TelegramApiException e) {
                throw new RuntimeException("Failed to initialize Telegram receiver", e);
            }
        }
        return null;  // No receiver in mock mode
    }
}
