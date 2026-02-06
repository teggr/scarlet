package dev.rebelcraft.scarlet.web;

import dev.rebelcraft.scarlet.telegram.ChatHistory;
import dev.rebelcraft.scarlet.telegram.ChatManager;
import dev.rebelcraft.scarlet.telegram.ChatMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ChatManager chatManager;

    public ConversationController(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @GetMapping
    public Collection<ChatHistory> getAllConversations() {
        return chatManager.getAllChatHistories();
    }

    @GetMapping("/{chatId}")
    public ChatHistory getConversation(@PathVariable Long chatId) {
        return chatManager.getChatHistory(chatId);
    }

    @GetMapping("/{chatId}/messages")
    public List<ChatMessage> getConversationMessages(@PathVariable Long chatId) {
        ChatHistory history = chatManager.getChatHistory(chatId);
        return history != null ? history.getMessages() : List.of();
    }
}
