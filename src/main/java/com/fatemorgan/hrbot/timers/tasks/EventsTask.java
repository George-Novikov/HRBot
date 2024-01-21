package com.fatemorgan.hrbot.timers.tasks;

import com.fatemorgan.hrbot.services.TelegramBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class EventsTask extends TimerTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsTask.class);

    private TelegramBotService bot;

    public EventsTask(TelegramBotService bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        try {
            bot.processTomorrowEvents();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }
}
