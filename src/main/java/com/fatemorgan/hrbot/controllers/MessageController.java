package com.fatemorgan.hrbot.controllers;

import com.fatemorgan.hrbot.handlers.HRBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.UUID;

@RestController
@RequestMapping("/message")
public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private HRBot bot;

    public MessageController(HRBot bot) {
        this.bot = bot;
    }

    @GetMapping
    public void message(){
        try {
            String message = String.format("Message: %s - %s", LocalTime.now(), UUID.randomUUID());
            bot.sendMessage(message);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }
}
