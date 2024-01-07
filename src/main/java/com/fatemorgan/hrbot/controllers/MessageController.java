package com.fatemorgan.hrbot.controllers;

import com.fatemorgan.hrbot.handlers.HRBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private HRBot bot;

    public MessageController(HRBot bot) {
        this.bot = bot;
    }

    @GetMapping(path = "/send")
    public void message(@RequestParam(value = "text", defaultValue = "") String text){
        try {
            String response = bot.sendMessage(text);
            if (response != null) LOGGER.info(response);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }
}
