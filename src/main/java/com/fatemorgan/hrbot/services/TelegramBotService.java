package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.handlers.TelegramApi;
import com.fatemorgan.hrbot.model.telegram.response.TelegramResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TelegramBotService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotService.class);
    @Value("${telegram.url}")
    private String url;
    @Value("${telegram.bot-token}")
    private String botToken;
    @Value("${telegram.chat-id}")
    private Long chatID;

    private TelegramApi api;

    public TelegramBotService(TelegramApi api) {
        this.api = api;
    }

    public String sendMessage(String message) throws Exception {
        return api.sendMessage(message);
    }

    public String reply(String message, Long repliedMessageID) throws Exception {
        return api.reply(message, repliedMessageID);
    }

    public TelegramResponse getUpdates() {
        return api.getUpdates();
    }

    public String replyUnanswered() throws Exception {
        return api.replyUnanswered();
    }
}
