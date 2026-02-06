package dev.rebelcraft.scarlet.telegram;

import dev.rebelcraft.telegram.ChatManager;
import dev.rebelcraft.telegram.TelegramListener;
import dev.rebelcraft.telegram.TelegramListenerContainer;
import dev.rebelcraft.telegram.TelegramTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Configuration
public class TelegramConfiguration {

  @Value("${TELEGRAM_BOT_TOKEN:}")
  public String telegramBotToken;

  @Bean(initMethod = "start", destroyMethod = "stop")
  @ConditionalOnProperty(name = "telegram.enabled", havingValue = "true")
  public TelegramListenerContainer telegramListenerContainer( List<TelegramListener> telegramListeners ) {
    return new TelegramListenerContainer(telegramBotToken, telegramListeners );
  }

  @Bean
  @ConditionalOnProperty(name = "telegram.enabled", havingValue = "true")
  public TelegramTemplate telegramTemplate() {
    return new TelegramTemplate(telegramBotToken);
  }

  @Bean
  @ConditionalOnProperty(name = "telegram.enabled", havingValue = "true")
  public TelegramListener telegramListener( TelegramTemplate telegramTemplate ) {
    return update -> {

      Long chatId = update.getMessage().getChatId();
      String messageText = update.getMessage().getText();
      String senderName = update.getMessage().getFrom().getFirstName();

      System.out.println("Received message from " + senderName + " in chat " + chatId + ": " + messageText);

      telegramTemplate.sendResponse( chatId, "Thinking about:" + messageText );

    };
  }

  @Bean
  public ChatManager chatManager() {
    return new ChatManager();
  }

}
