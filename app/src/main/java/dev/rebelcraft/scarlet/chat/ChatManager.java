package dev.rebelcraft.scarlet.chat;

import java.util.Collection;
import java.util.List;

/**
 * Manages chat histories for all active conversations.
 * Stores chat histories in memory.
 */
public class ChatManager {

  private final List<ChatHistoryProvider> providers;

  public ChatManager(List<ChatHistoryProvider> providers) {
    this.providers = providers;
  }

  /**
   * Get the chat history for a specific chat ID.
   */
  public ChatHistory getChatHistory(Long chatId) {
    return providers.stream()
      .filter(cp -> cp.hasChat(chatId))
      .findFirst()
      .map(cp -> cp.getChat(chatId))
      .orElse(null);
  }

  /**
   * Get all chat histories.
   */
  public Collection<ChatHistory> getAllChatHistories() {
    return providers.stream()
      .flatMap(cp -> cp.getAll().stream())
      .toList();
  }

}
