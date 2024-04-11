package org.talos.emojitestspringbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.talos.emojitestspringbot.bot.EmojiBot;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class EmojiBotConfig {
    @Bean
    public TelegramBotsLongPollingApplication telegramBots(
            @Value("${bot.token}")
            String botToken, EmojiBot emojiBot
    ) throws TelegramApiException {
        var api = new TelegramBotsLongPollingApplication();
        api.registerBot(botToken, emojiBot);
        return api;
    }
}
