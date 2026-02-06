package dev.rebelcraft.scarlet.telegram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores the conversation history for a single chat.
 */
public class ChatHistory {
    
    private final Long chatId;
    private final List<ChatMessage> messages;
    private String chatTitle;
    
    public ChatHistory(Long chatId) {
        this.chatId = chatId;
        this.messages = new ArrayList<>();
        this.chatTitle = "Chat " + chatId;
    }
    
    public void addMessage(ChatMessage message) {
        messages.add(message);
    }
    
    public Long getChatId() {
        return chatId;
    }
    
    public List<ChatMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }
    
    public String getChatTitle() {
        return chatTitle;
    }
    
    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }
    
    public int getMessageCount() {
        return messages.size();
    }
    
    public ChatMessage getLastMessage() {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.get(messages.size() - 1);
    }
}
