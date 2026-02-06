package dev.rebelcraft.scarlet;

import dev.rebelcraft.scarlet.telegram.ChatManager;
import dev.rebelcraft.scarlet.telegram.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

  @Value("${TELEGRAM_BOT_TOKEN:}")
  public String BOT_TOKEN;

  @Bean
  public ChatManager chatManager() {
    return new ChatManager();
  }

  @Bean
  @ConditionalOnProperty(name = "TELEGRAM_BOT_TOKEN")
  public LongPollingUpdateConsumer telegramBot(ChatManager chatManager) {
    return new LongPollingSingleThreadUpdateConsumer() {

      private TelegramClient telegramClient = new OkHttpTelegramClient(BOT_TOKEN);

      @Override
      public void consume(Update update) {

        System.out.println("Received update: " + update);

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
          System.out.println("Received update: " + update.getMessage().getText());

          Long chatId = update.getMessage().getChatId();
          String messageText = update.getMessage().getText();
          String senderName = update.getMessage().getFrom().getFirstName();

          // Add incoming message to chat history
          ChatMessage incomingMessage = new ChatMessage(chatId, messageText, true, senderName);
          chatManager.addMessage(incomingMessage);

          // Create your send message object
          SendMessage sendMessage = new SendMessage(String.valueOf(chatId), messageText);
          try {
            // Execute it
            telegramClient.execute(sendMessage);
            
            // Add sent message to chat history
            ChatMessage outgoingMessage = new ChatMessage(chatId, messageText, false, "Bot");
            chatManager.addMessage(outgoingMessage);
          } catch (TelegramApiException e) {
            e.printStackTrace();
          }

        }

      }

    };
  }

  @Bean
  @ConditionalOnProperty(name = "TELEGRAM_BOT_TOKEN")
  public TelegramBotsLongPollingApplication telegramBotsLongPollingApplication(LongPollingUpdateConsumer telegramBot) throws TelegramApiException {
    TelegramBotsLongPollingApplication longPollingApplication = new TelegramBotsLongPollingApplication();
    longPollingApplication.registerBot(BOT_TOKEN, telegramBot);
    System.out.println("Telegram bot registered successfully.");
    return longPollingApplication;
  }

}
