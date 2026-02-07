package dev.rebelcraft.scarlet.chat;

import java.util.List;

public interface ChatHistoryProvider {

  boolean hasChat(Long chatId);

  ChatHistory getChat(Long chatId);

  List<ChatHistory> getAll();

}
