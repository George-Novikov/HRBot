package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.network.Responder;
import com.fatemorgan.hrbot.workers.TelegramApi;
import com.fatemorgan.hrbot.model.chat.ChatReplies;
import com.fatemorgan.hrbot.model.telegram.response.TelegramResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.fatemorgan.hrbot.model.constants.ChatMessage.EMPTY_CHAT_REPLIES;

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
    private ChatService chatService;

    public TelegramBotService(TelegramApi api, ChatService chatService) {
        this.api = api;
        this.chatService = chatService;
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

    public ResponseEntity replyUnanswered() throws Exception {
        ChatReplies chatReplies = chatService.getChatReplies();
        if (chatReplies == null || chatReplies.isEmpty()) return Responder.sendError(EMPTY_CHAT_REPLIES);
        return Responder.sendOk(api.replyUnanswered(chatReplies));
    }
}
