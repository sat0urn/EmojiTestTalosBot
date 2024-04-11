package org.talos.emojitestspringbot.bot;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
public class EmojiBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    @Autowired
    public EmojiBot(@Value("${bot.token}") String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            long user_id = update.getMessage().getChat().getId();

            if (update.getMessage().hasText()) {
                String message = update.getMessage().getText();
                long chat_id = update.getMessage().getChatId();

                if (message.equals("/start")) {
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
                } else if (message.equals("/pic")) {
                    String answer = EmojiParser.parseToUnicode("Twenty One Pilot :fire:");

                    SendPhoto sendPhoto = SendPhoto
                            .builder()
                            .chatId(chat_id)
                            .photo(new InputFile("https://yt3.googleusercontent.com/WAPIdR1wIIn7LJThOA9HCCWZi1HMEJYWhWc93LOj9-lq246Kt8IuFG3x4ruIiR4WWiDrpk31_Q=s176-c-k-c0x00ffffff-no-rj"))
                            .caption(answer)
                            .build();

                    log(user_first_name, user_last_name, Long.toString(user_id), message, answer);

                    try {
                        telegramClient.execute(sendPhoto);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (message.equals("/markup")) {
                    String answer = EmojiParser.parseToUnicode("Here is your keyboard :keyboard:");

                    SendMessage sendMessage = SendMessage
                            .builder()
                            .chatId(chat_id)
                            .text(answer)
                            .build();

                    sendMessage.setReplyMarkup(
                            ReplyKeyboardMarkup
                                    .builder()
                                    .keyboardRow(new KeyboardRow("Button 1", "Button 2"))
                                    .keyboardRow(new KeyboardRow("Button 3", "Button 4"))
                                    .build()
                    );

                    log(user_first_name, user_last_name, Long.toString(user_id), message, answer);

                    try {
                        telegramClient.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (message.equals("Button 4")) {
                    String caption = EmojiParser.parseToUnicode("Jack Stauber :thinking:");

                    SendPhoto sendPhoto = SendPhoto
                            .builder()
                            .chatId(chat_id)
                            .photo(new InputFile("https://yt3.googleusercontent.com/ytc/AIdro_kmBu6jJWaWfHS4nCX52KSHoBTdTBsd63drA5D54WV3Ug=s176-c-k-c0x00ffffff-no-rj"))
                            .caption(caption)
                            .build();

                    log(user_first_name, user_last_name, Long.toString(user_id), message, caption);

                    try {
                        telegramClient.execute(sendPhoto);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (message.equals("/hide")) {
                    String answer = EmojiParser.parseToUnicode("Keyboard is hidden :face_with_peeking_eye:");

                    SendMessage sendMessage = SendMessage
                            .builder()
                            .chatId(chat_id)
                            .text(answer)
                            .build();

                    sendMessage.setReplyMarkup(ReplyKeyboardRemove.builder().build());

                    log(user_first_name, user_last_name, Long.toString(user_id), message, answer);

                    try {
                        telegramClient.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {
                    String answer = EmojiParser.parseToUnicode("I CAN'T UNDERSTAND YOU :rage:");

                    SendMessage sendMessage = SendMessage
                            .builder()
                            .chatId(chat_id)
                            .text(answer)
                            .build();

                    log(user_first_name, user_last_name, Long.toString(user_id), message, answer);

                    try {
                        telegramClient.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else if (update.getMessage().hasPhoto()) {
                String message = update.getMessage().getText();
                long chat_id = update.getMessage().getChatId();

                List<PhotoSize> photos = update.getMessage().getPhoto();

                String f_id = photos.stream()
                        .max(Comparator.comparing(PhotoSize::getFileSize))
                        .map(PhotoSize::getFileId)
                        .orElse("");
                int f_width = photos.stream()
                        .max(Comparator.comparing(PhotoSize::getFileSize))
                        .map(PhotoSize::getWidth)
                        .orElse(0);
                int f_height = photos.stream()
                        .max(Comparator.comparing(PhotoSize::getFileSize))
                        .map(PhotoSize::getHeight)
                        .orElse(0);

                String caption = String.format("""
                        Id: %s
                        Width: %d
                        Height: %d
                        """, f_id, f_width, f_height);

                SendPhoto sendPhoto = SendPhoto
                        .builder()
                        .chatId(chat_id)
                        .photo(new InputFile(f_id))
                        .caption(caption)
                        .build();

                log(user_first_name, user_last_name, Long.toString(user_id), message, caption);

                try {
                    telegramClient.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
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
