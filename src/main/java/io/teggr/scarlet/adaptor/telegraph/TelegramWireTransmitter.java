package io.teggr.scarlet.adaptor.telegraph;

import io.teggr.scarlet.domain.conduit.TelegraphicDispatcher;
import io.teggr.scarlet.domain.nucleus.TransmissionEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramWireTransmitter extends TelegramLongPollingBot implements TelegraphicDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(TelegramWireTransmitter.class);
    
    private final String botUsername;
    private final String botToken;

    public TelegramWireTransmitter(String botUsername, String botToken) {
        super(botToken);
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(org.telegram.telegrambots.meta.api.objects.Update update) {
        // Handled by receiver component
    }

    @Override
    public void propagate(TransmissionEnvelope envelope) {
        SendMessage telegraph = SendMessage.builder()
                .chatId(envelope.destinationNode())
                .text(envelope.payloadContent())
                .build();

        try {
            execute(telegraph);
            logger.info("Propagated transmission to node: {}", envelope.destinationNode());
        } catch (TelegramApiException exception) {
            logger.error("Failed to propagate transmission", exception);
        }
    }
}
