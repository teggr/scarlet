package dev.rebelcraft.scarlet.telegram;

import dev.rebelcraft.scarlet.chat.ChatHistory;
import dev.rebelcraft.scarlet.chat.ChatMessage;
import dev.rebelcraft.telegram.StoredMessage;

import java.util.ArrayList;
import java.util.List;

class TelegramChatHistory implements ChatHistory {

  private final Long chatId;
  private final List<ChatMessage> messages = new ArrayList<>();

  public TelegramChatHistory(Long chatId, List<StoredMessage> messages) {
    this.chatId = chatId;
    for (StoredMessage storedMessage : messages) {
      this.messages.add(new ChatMessage(
        chatId,
        storedMessage.message(),
        storedMessage.timestamp(),
        storedMessage.isIncoming(),
        storedMessage.senderName()
      ));
    }
  }

  @Override
  public Long getChatId() {
    return chatId;
  }

  @Override
  public List<ChatMessage> getMessages() {
    return messages;
  }

  @Override
  public String getChatTitle() {
    return "Telegram Chat " + chatId;
  }

  @Override
  public int getMessageCount() {
    return messages.size();
  }

}
