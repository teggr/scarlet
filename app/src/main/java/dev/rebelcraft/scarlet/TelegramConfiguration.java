package dev.rebelcraft.scarlet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfiguration {

  @Value("${TELEGRAM_BOT_TOKEN}")
  public String BOT_TOKEN;

  @Bean
  public LongPollingUpdateConsumer telegramBot() {
    return new LongPollingSingleThreadUpdateConsumer() {

      private TelegramClient telegramClient = new OkHttpTelegramClient(BOT_TOKEN);

      @Override
      public void consume(Update update) {

        System.out.println("Received update: " + update);

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
          System.out.println("Received update: " + update.getMessage().getText());



          // Create your send message object
          SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()),
            update.getMessage().getText());
          try {
            // Execute it
            telegramClient.execute(sendMessage);
          } catch (TelegramApiException e) {
            e.printStackTrace();
          }

        }

      }

    };
  }

  @Bean
  public TelegramBotsLongPollingApplication telegramBotsLongPollingApplication(LongPollingUpdateConsumer telegramBot) throws TelegramApiException {
    TelegramBotsLongPollingApplication longPollingApplication = new TelegramBotsLongPollingApplication();
    longPollingApplication.registerBot(BOT_TOKEN, telegramBot);
    System.out.println("Telegram bot registered successfully.");
    return longPollingApplication;
  }

}
