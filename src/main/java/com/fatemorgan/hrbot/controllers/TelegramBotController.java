package com.fatemorgan.hrbot.controllers;

import com.fatemorgan.hrbot.network.Responder;
import com.fatemorgan.hrbot.services.TelegramBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bot")
public class TelegramBotController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotController.class);

    private TelegramBotService bot;

    public TelegramBotController(TelegramBotService bot) {
        this.bot = bot;
    }

    @GetMapping(path = "/info")
    public ResponseEntity getBotName(){
        try {
            return Responder.sendOk(bot.getBotInfo());
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @GetMapping(path = "/messages/send")
    public ResponseEntity sendMessage(@RequestParam(value = "text", defaultValue = "") String text){
        try {
            return ResponseEntity.ok(bot.sendMessage(text));
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return  Responder.sendError(e);
        }
    }

    @GetMapping(path = "/messages/reply")
    public ResponseEntity reply(
            @RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(value = "reply_message_id", defaultValue = "0") Long replyMessageID
    ){
        try {
            return ResponseEntity.ok(bot.reply(text, replyMessageID));
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @GetMapping(path = "/messages/replyUnanswered")
    public ResponseEntity replyUnanswered(){
        try {
            return Responder.sendOk(bot.replyUnanswered());
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @GetMapping(path = "/messages/getUpdates")
    public ResponseEntity getUpdates(){
        try {
            return ResponseEntity.ok(bot.getUpdates());
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @GetMapping(path = "/birthdays/process/current")
    public ResponseEntity processCurrentBirthdays(){
        try {
            String response = bot.processCurrentBirthdays();
            return Responder.sendOk(response);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @GetMapping(path = "/events/today")
    public ResponseEntity processTodayEvents(){
        try {
            String response = bot.processTodayEvents();
            return Responder.sendOk(response);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }
}
