package dev.rebelcraft.scarlet.telegram;

import dev.rebelcraft.scarlet.chat.ChatHistoryProvider;
import dev.rebelcraft.telegram.*;
import dev.rebelcraft.telegram.store.InMemoryTelegramMessageStore;
import dev.rebelcraft.telegram.store.StoreTelegramOperations;
import dev.rebelcraft.telegram.store.StoreUpdatesListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TelegramConfiguration {

  @Value("${TELEGRAM_BOT_TOKEN:}")
  public String telegramBotToken;

  @Bean(initMethod = "start", destroyMethod = "stop")
  @ConditionalOnProperty(name = "telegram.enabled", havingValue = "true")
  public TelegramListenerContainer telegramListenerContainer(List<TelegramListener> telegramListeners) {
    return new TelegramListenerContainer(telegramBotToken, telegramListeners);
  }

  @Bean
  @ConditionalOnProperty(name = "telegram.enabled", havingValue = "true")
  public TelegramOperations telegramTemplate(TelegramMessageStore telegramMessageStore) {
    return new StoreTelegramOperations(new TelegramTemplate(telegramBotToken), telegramMessageStore);
  }

  @Bean
  public TelegramMessageStore telegramMessageStore() {
    return new InMemoryTelegramMessageStore();
  }

  @Bean
  @ConditionalOnProperty(name = "telegram.enabled", havingValue = "true")
  public TelegramListener telegramListener(TelegramOperations telegramOperations, TelegramMessageStore telegramMessageStore) {
    TelegramListener telegramListener = update -> {

      Long chatId = update.getMessage().getChatId();
      String messageText = update.getMessage().getText();
      String senderName = update.getMessage().getFrom().getFirstName();

      System.out.println("Received message from " + senderName + " in chat " + chatId + ": " + messageText);

      telegramOperations.sendResponse(chatId, "Thinking about:" + messageText);

    };
    return new StoreUpdatesListener(telegramListener, telegramMessageStore);
  }

  @Bean
  public ChatHistoryProvider telegramChatHistoryProvider(TelegramMessageStore telegramMessageStore) {
    return new TelegramChatHistoryProvider(telegramMessageStore);
  }

}
