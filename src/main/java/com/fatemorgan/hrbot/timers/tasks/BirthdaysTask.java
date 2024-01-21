package com.fatemorgan.hrbot.timers.tasks;

import com.fatemorgan.hrbot.services.TelegramBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class BirthdaysTask extends TimerTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(BirthdaysTask.class);
    private TelegramBotService bot;

    public BirthdaysTask(TelegramBotService bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        LOGGER.info("BirthdaysTask is working");
        try {
            bot.processCurrentBirthdays();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }
}
