package dev.rebelcraft.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramListener {
  void onUpdate(Update update);
}
