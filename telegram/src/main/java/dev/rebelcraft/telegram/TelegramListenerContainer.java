package dev.rebelcraft.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.util.List;

public class TelegramListenerContainer {

  private static final Logger log = LoggerFactory.getLogger(TelegramListenerContainer.class);

  private boolean isRunning = false;
  private final String botToken;
  private final List<TelegramListener> listeners;
    private TelegramBotsLongPollingApplication longPollingApplication;

  public TelegramListenerContainer(String botToken, List<TelegramListener> listeners ) {
    this.botToken = botToken;
    this.listeners = listeners;
  }

  public void start() throws TelegramApiException {

    log.info("Starting TelegramListenerContainer");

    LongPollingUpdateConsumer consumer = new LongPollingSingleThreadUpdateConsumer() {

      @Override
      public void consume(Update update) {

        System.out.println("Received update: " + update);

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
          System.out.println("Received update: " + update.getMessage().getText());

          Long chatId = update.getMessage().getChatId();
          String messageText = update.getMessage().getText();
          String senderName = update.getMessage().getFrom().getFirstName();

          listeners.forEach( l -> l.onUpdate(update) );

        }

      }

    };

    longPollingApplication = new TelegramBotsLongPollingApplication();
    longPollingApplication.registerBot(botToken, consumer);

    log.info("Started TelegramListenerContainer");

    isRunning = true;
  }

  public void stop() throws TelegramApiException {

    log.info("Stopping TelegramListenerContainer");

    if( longPollingApplication != null ) {
      longPollingApplication.stop();
    }
    isRunning = false;

    log.info("Stopped TelegramListenerContainer");

  }

  public boolean isRunning() {
    return isRunning;
  }

}
