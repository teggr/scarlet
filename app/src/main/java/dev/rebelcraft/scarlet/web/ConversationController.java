package dev.rebelcraft.scarlet.web;

import dev.rebelcraft.scarlet.chat.ChatHistory;
import dev.rebelcraft.scarlet.chat.ChatManager;
import dev.rebelcraft.scarlet.chat.ChatMessage;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ChatHistory> getConversation(@PathVariable Long chatId) {
        ChatHistory history = chatManager.getChatHistory(chatId);
        if (history == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<ChatMessage>> getConversationMessages(@PathVariable Long chatId) {
        ChatHistory history = chatManager.getChatHistory(chatId);
        if (history == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(history.getMessages());
    }
}
