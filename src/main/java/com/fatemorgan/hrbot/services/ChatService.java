package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.model.GlobalDataContainer;
import com.fatemorgan.hrbot.model.chat.ChatReplies;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.constants.BirthdaysMessage;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.exceptions.SettingsException;
import com.fatemorgan.hrbot.network.Responder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ChatService {
    private GoogleSheetsService googleSheetsService;
    private GlobalDataContainer globalContainer;

    public ChatService(GoogleSheetsService googleSheetsService, GlobalDataContainer globalContainer) {
        this.googleSheetsService = googleSheetsService;
        this.globalContainer = globalContainer;
    }

    public ResponseEntity getChatReplies() throws IOException, SettingsException, DateParserException {
        googleSheetsService.updateGlobalContainer(Action.CHAT_UPDATE);

        ChatReplies chatReplies = globalContainer.getChatReplies();
        if (chatReplies == null) return Responder.sendError(BirthdaysMessage.BIRTHDAYS_LOADING_ERROR);

        return Responder.sendOk(chatReplies);
    }

    public ResponseEntity getReplyText(String request) throws IOException, SettingsException, DateParserException {
        googleSheetsService.updateGlobalContainer(Action.CHAT_UPDATE);

        ChatReplies chatReplies = globalContainer.getChatReplies();
        if (chatReplies == null) return Responder.sendError(BirthdaysMessage.BIRTHDAYS_LOADING_ERROR);

        return Responder.sendOk(chatReplies.getReplyText(request));
    }
}
