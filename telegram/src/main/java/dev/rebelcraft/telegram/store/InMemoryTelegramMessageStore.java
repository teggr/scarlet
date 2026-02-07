package dev.rebelcraft.telegram.store;

import dev.rebelcraft.telegram.StoredMessage;
import dev.rebelcraft.telegram.TelegramMessageStore;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTelegramMessageStore implements TelegramMessageStore {

  private final Map<Long, List<StoredMessage>> chatMessages = new HashMap<>();

  @Override
  public void onSendMessage(Long chatId, String message) {
    StoredMessage storedMessage = new StoredMessage(
        chatId,
        message,
        Instant.now(),
        false,
        "Bot"
    );
    chatMessages.computeIfAbsent(chatId, k -> new ArrayList<>()).add(storedMessage);
  }

  @Override
  public void onUpdate(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      Long chatId = update.getMessage().getChatId();
      String message = update.getMessage().getText();
      String senderName = update.getMessage().getFrom().getFirstName();

      StoredMessage storedMessage = new StoredMessage(
          chatId,
          message,
          Instant.now(),
          true,
          senderName
      );
      chatMessages.computeIfAbsent(chatId, k -> new ArrayList<>()).add(storedMessage);
    }
  }

  @Override
  public boolean hasChat(Long chatId) {
    return chatMessages.containsKey(chatId);
  }

  @Override
  public List<StoredMessage> getMessages(Long chatId) {
    return chatMessages.getOrDefault(chatId, List.of());
  }

  @Override
  public Map<Long, List<StoredMessage>> getAllMessages() {
    return Map.copyOf(chatMessages);
  }

}
