package dev.rebelcraft.scarlet.chat;

import java.util.List;

/**
 * Stores the conversation history for a single chat.
 * Thread-safe for concurrent message additions.
 */
public interface ChatHistory {

     Long getChatId();
    
     List<ChatMessage> getMessages();
    
     String getChatTitle();

     int getMessageCount();
    
     default ChatMessage getLastMessage() {
        if (getMessages().isEmpty()) {
            return null;
        }
        return getMessages().get(getMessages().size() - 1);
    }

}
