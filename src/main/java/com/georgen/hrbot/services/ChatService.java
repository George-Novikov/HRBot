package com.georgen.hrbot.services;

import com.georgen.hrbot.model.DataGlobalContainer;
import com.georgen.hrbot.model.chat.ChatReplies;
import com.georgen.hrbot.model.constants.Action;
import com.georgen.hrbot.model.constants.BirthdaysMessage;
import com.georgen.hrbot.model.exceptions.DateParserException;
import com.georgen.hrbot.model.exceptions.SettingsException;
import com.georgen.hrbot.network.Responder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ChatService {
    private GoogleSheetsService googleSheetsService;
    private DataGlobalContainer globalContainer;

    public ChatService(GoogleSheetsService googleSheetsService, DataGlobalContainer globalContainer) {
        this.googleSheetsService = googleSheetsService;
        this.globalContainer = globalContainer;
    }

    public ChatReplies getChatReplies() throws IOException, SettingsException, DateParserException {
        googleSheetsService.updateGlobalContainer(Action.CHAT_UPDATE);
        return globalContainer.getChatReplies();
    }

    public ResponseEntity getReplyText(String request) throws IOException, SettingsException, DateParserException {
        googleSheetsService.updateGlobalContainer(Action.CHAT_UPDATE);

        ChatReplies chatReplies = globalContainer.getChatReplies();
        if (chatReplies == null) return Responder.sendError(BirthdaysMessage.BIRTHDAYS_LOADING_ERROR);

        return Responder.sendOk(chatReplies.getReply(request));
    }


}
