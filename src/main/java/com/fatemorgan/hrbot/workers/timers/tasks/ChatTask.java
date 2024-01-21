package com.fatemorgan.hrbot.workers.timers.tasks;


import com.fatemorgan.hrbot.services.TelegramBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class ChatTask extends TimerTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(BirthdaysTask.class);
    private TelegramBotService bot;

    public ChatTask(TelegramBotService bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        LOGGER.info("ChatTask is working");
        try {
            bot.replyUnanswered();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }
}
