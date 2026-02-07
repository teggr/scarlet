package dev.rebelcraft.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

public interface TelegramMessageStore {

  void onSendMessage(Long chatId, String message);

  void onUpdate(Update update);

  boolean hasChat(Long chatId);

  List<StoredMessage> getMessages(Long chatId);

  Map<Long, List<StoredMessage>> getAllMessages();
}
