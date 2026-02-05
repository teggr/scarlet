package io.teggr.scarlet;

import io.teggr.scarlet.adaptor.chronicle.SimpleMemoryVault;
import io.teggr.scarlet.adaptor.telegraph.TelegramWireReceiver;
import io.teggr.scarlet.adaptor.telegraph.TelegramWireTransmitter;
import io.teggr.scarlet.domain.conduit.TelegraphicDispatcher;
import io.teggr.scarlet.domain.conduit.UtteranceChronicle;
import io.teggr.scarlet.domain.nucleus.ConversationConductor;
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
    public TelegraphicDispatcher telegraphicDispatcher() throws TelegramApiException {
        TelegramWireTransmitter transmitter = new TelegramWireTransmitter(telegramBotUsername, telegramBotToken);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(transmitter);
        return transmitter;
    }

    @Bean
    public ConversationConductor conversationConductor(UtteranceChronicle chronicle, 
                                                       TelegraphicDispatcher dispatcher) {
        return new ConversationConductor(chronicle, dispatcher);
    }

    @Bean
    public TelegramWireReceiver telegramWireReceiver(ConversationConductor conductor) throws TelegramApiException {
        TelegramWireReceiver receiver = new TelegramWireReceiver(telegramBotUsername, telegramBotToken, conductor);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(receiver);
        return receiver;
    }
}
