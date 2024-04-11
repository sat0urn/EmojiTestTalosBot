package org.talos.emojitestspringbot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class EmojiBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    @Autowired
    public EmojiBot(@Value("${bot.token}") String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            long user_id = update.getMessage().getChat().getId();

            String message = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            SendMessage sendMessage = SendMessage
                    .builder()
                    .chatId(chat_id)
                    .text(message)
                    .build();

            log(user_first_name, user_last_name, Long.toString(user_id), message, message);

            try {
                telegramClient.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void log(String first_name, String last_name, String user_id, String text, String bot_answer) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        System.out.printf(
                """
                %s
                -----------------------------------
                Message from %s %s. (id = %s)
                Text - %s
                 Bot answer:
                Text - %s
                """, dateFormat.format(date), first_name, last_name, user_id, text, bot_answer);
    }
}
