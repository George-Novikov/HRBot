package com.georgen.hrbot.controllers;

import com.georgen.hrbot.model.chat.ChatReplies;
import com.georgen.hrbot.model.constants.BirthdaysMessage;
import com.georgen.hrbot.network.Responder;
import com.georgen.hrbot.services.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
    private ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    @GetMapping("/replies")
    public ResponseEntity getChatReplies(){
        try {
            ChatReplies chatReplies = service.getChatReplies();
            if (chatReplies == null) return Responder.sendError(BirthdaysMessage.BIRTHDAYS_LOADING_ERROR);
            return Responder.sendOk(chatReplies);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @GetMapping("/replies/getReplyText")
    public ResponseEntity getReplyText(
            @RequestParam(value = "request", defaultValue = "") String request
    ){
        try {
            return service.getReplyText(request);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }
}
