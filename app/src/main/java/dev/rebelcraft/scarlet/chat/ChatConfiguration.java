package dev.rebelcraft.scarlet.chat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatConfiguration {

  @Bean
  public ChatManager chatManager(List<ChatHistoryProvider> providers) {
    return new ChatManager(providers);
  }

}
