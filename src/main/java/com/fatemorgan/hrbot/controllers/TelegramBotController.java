package com.fatemorgan.hrbot.controllers;

import com.fatemorgan.hrbot.services.TelegramBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/message")
public class TelegramBotController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotController.class);

    private TelegramBotService bot;

    public TelegramBotController(TelegramBotService bot) {
        this.bot = bot;
    }

    @GetMapping(path = "/send")
    public ResponseEntity sendMessage(@RequestParam(value = "text", defaultValue = "") String text){
        try {
            return ResponseEntity.ok(bot.sendMessage(text));
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping(path = "/reply")
    public ResponseEntity reply(
            @RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(value = "reply_message_id", defaultValue = "0") Long replyMessageID
    ){
        try {
            return ResponseEntity.ok(bot.reply(text, replyMessageID));
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping(path = "/replyUnanswered")
    public ResponseEntity replyUnanswered(){
        try {
            return ResponseEntity.ok(bot.replyUnanswered());
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping(path = "/getUpdates")
    public ResponseEntity getUpdates(){
        try {
            return ResponseEntity.ok(bot.getUpdates());
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
