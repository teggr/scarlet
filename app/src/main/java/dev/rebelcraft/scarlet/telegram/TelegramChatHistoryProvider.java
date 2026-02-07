package dev.rebelcraft.scarlet.telegram;

import dev.rebelcraft.scarlet.chat.ChatHistory;
import dev.rebelcraft.scarlet.chat.ChatHistoryProvider;
import dev.rebelcraft.telegram.TelegramMessageStore;

import java.util.List;

class TelegramChatHistoryProvider implements ChatHistoryProvider {

  private final TelegramMessageStore telegramMessageStore;

  public TelegramChatHistoryProvider(TelegramMessageStore telegramMessageStore) {
    this.telegramMessageStore = telegramMessageStore;
  }

  @Override
  public boolean hasChat(Long chatId) {
    return telegramMessageStore.hasChat(chatId);
  }

  @Override
  public ChatHistory getChat(Long chatId) {
    return new TelegramChatHistory(chatId, telegramMessageStore
      .getMessages(chatId));
  }

  @Override
  public List<ChatHistory> getAll() {
    return telegramMessageStore.getAllMessages().entrySet()
      .stream()
      .map(entry -> (ChatHistory) new TelegramChatHistory(entry.getKey(), entry.getValue()))
      .toList();
  }

}
