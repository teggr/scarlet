package dev.rebelcraft.scarlet.telegram;

import java.time.Instant;

/**
 * Represents a single message in a chat conversation.
 */
public class ChatMessage {
    
    private final Long chatId;
    private final String messageText;
    private final Instant timestamp;
    private final boolean isIncoming;
    private final String senderName;
    
    public ChatMessage(Long chatId, String messageText, boolean isIncoming, String senderName) {
        this.chatId = chatId;
        this.messageText = messageText;
        this.timestamp = Instant.now();
        this.isIncoming = isIncoming;
        this.senderName = senderName;
    }
    
    public Long getChatId() {
        return chatId;
    }
    
    public String getMessageText() {
        return messageText;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public boolean isIncoming() {
        return isIncoming;
    }
    
    public String getSenderName() {
        return senderName;
    }
    
    @Override
    public String toString() {
        return "ChatMessage{" +
                "chatId=" + chatId +
                ", messageText='" + messageText + '\'' +
                ", timestamp=" + timestamp +
                ", isIncoming=" + isIncoming +
                ", senderName='" + senderName + '\'' +
                '}';
    }
}
