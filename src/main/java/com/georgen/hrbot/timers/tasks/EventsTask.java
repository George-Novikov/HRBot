package com.georgen.hrbot.timers.tasks;

import com.georgen.hrbot.services.TelegramBotService;
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
            bot.processTodayEvents();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }
}
