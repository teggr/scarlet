package io.teggr.scarlet.adaptor.telegraph;

import io.teggr.scarlet.domain.nucleus.ConversationConductor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;

public class TelegramWireReceiver extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(TelegramWireReceiver.class);
    
    private final String botUsername;
    private final ConversationConductor conductor;

    public TelegramWireReceiver(String botUsername, String botToken, ConversationConductor conductor) {
        super(botToken);
        this.botUsername = botUsername;
        this.conductor = conductor;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message incomingTransmission = update.getMessage();
            String inscriptionContent = incomingTransmission.getText();
            String originatorTag = String.valueOf(incomingTransmission.getChatId());
            String conversationThread = originatorTag;

            logger.info("Absorbed utterance from originator: {}", originatorTag);
            conductor.absorbInboundUtterance(inscriptionContent, originatorTag, conversationThread);
        }
    }
}
