package com.fatemorgan.hrbot.controllers;

import com.fatemorgan.hrbot.network.Responder;
import com.fatemorgan.hrbot.services.ChatService;
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
            return service.getChatReplies();
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