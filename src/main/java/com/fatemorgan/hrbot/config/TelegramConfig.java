package com.fatemorgan.hrbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramConfig {
    @Value("${telegram.url}")
    private String url;
    @Value("${telegram.bot-token}")
    private String botToken;
    @Value("${telegram.chat-id}")
    private Long chatID;

    public String getUrl() {
        return url;
    }

    public String getBotToken() {
        return botToken;
    }

    public Long getChatID() {
        return chatID;
    }
}
