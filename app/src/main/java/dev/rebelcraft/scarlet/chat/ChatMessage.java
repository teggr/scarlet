package dev.rebelcraft.scarlet.chat;

import java.time.Instant;

/**
 * Represents a single message in a chat conversation.
 */
public record ChatMessage(

  Long chatId,
  String messageText,
  Instant timestamp,
  boolean incoming,
  String senderName

) {
}
