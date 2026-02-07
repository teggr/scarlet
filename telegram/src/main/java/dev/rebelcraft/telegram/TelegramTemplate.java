package dev.rebelcraft.telegram;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.Instant;

public class TelegramTemplate implements TelegramOperations {

  private final TelegramClient telegramClient;

  public TelegramTemplate(String botToken) {
    telegramClient  = new OkHttpTelegramClient(botToken);
  }

  @Override
  public void sendResponse(Long chatId, String message) {

    // Create your send message object
    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);
    try {
      // Execute it
      telegramClient.execute(sendMessage);

    } catch (TelegramApiException e) {
      e.printStackTrace();
    }

  }

}
