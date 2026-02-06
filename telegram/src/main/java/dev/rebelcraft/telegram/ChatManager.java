package dev.rebelcraft.telegram;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages chat histories for all active conversations.
 * Stores chat histories in memory.
 */
public class ChatManager {
    
    private final Map<Long, ChatHistory> chatHistories;
    
    public ChatManager() {
        this.chatHistories = new ConcurrentHashMap<>();
    }
    
    /**
     * Add a message to the appropriate chat history.
     * Creates a new chat history if one doesn't exist for this chat ID.
     */
    public void addMessage(ChatMessage message) {
        ChatHistory history = chatHistories.computeIfAbsent(
            message.chatId(),
            ChatHistory::new
        );
        history.addMessage(message);
    }
    
    /**
     * Get the chat history for a specific chat ID.
     */
    public ChatHistory getChatHistory(Long chatId) {
        return chatHistories.get(chatId);
    }
    
    /**
     * Get all chat histories.
     */
    public Collection<ChatHistory> getAllChatHistories() {
        return chatHistories.values();
    }
    
    /**
     * Check if a chat history exists for the given chat ID.
     */
    public boolean hasChatHistory(Long chatId) {
        return chatHistories.containsKey(chatId);
    }
}
