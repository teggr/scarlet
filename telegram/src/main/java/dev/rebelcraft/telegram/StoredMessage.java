package dev.rebelcraft.telegram;

import java.time.Instant;

public record StoredMessage(
  Long chatId,
  String message,
  Instant timestamp,
  boolean isIncoming,
  String senderName
) {
}
