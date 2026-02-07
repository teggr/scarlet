package dev.rebelcraft.telegram.store;

import dev.rebelcraft.telegram.TelegramMessageStore;
import dev.rebelcraft.telegram.TelegramOperations;

public class StoreTelegramOperations implements TelegramOperations {

  private final TelegramOperations telegramOperations;
  private final TelegramMessageStore messageStore;

  public StoreTelegramOperations(TelegramOperations telegramOperations, TelegramMessageStore messageStore) {
    this.telegramOperations = telegramOperations;
    this.messageStore = messageStore;
  }

  @Override
  public void sendResponse(Long chatId, String message) {

    try {
      this.telegramOperations.sendResponse(chatId, message);
    } finally {
      messageStore.onSendMessage( chatId, message );
    }

  }

}
