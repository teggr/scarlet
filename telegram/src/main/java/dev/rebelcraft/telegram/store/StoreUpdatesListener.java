package dev.rebelcraft.telegram.store;

import dev.rebelcraft.telegram.TelegramListener;
import dev.rebelcraft.telegram.TelegramMessageStore;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StoreUpdatesListener implements TelegramListener {

  private final TelegramListener telegramListener;
  private final TelegramMessageStore messageStore;

  public StoreUpdatesListener(TelegramListener telegramListener, TelegramMessageStore messageStore) {
    this.telegramListener = telegramListener;
    this.messageStore = messageStore;
  }

  @Override
  public void onUpdate(Update update) {
    try {
      this.telegramListener.onUpdate(update);
    } finally {
      messageStore.onUpdate(update);
    }
  }
}
