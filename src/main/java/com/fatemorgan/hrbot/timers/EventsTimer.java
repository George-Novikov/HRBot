package com.fatemorgan.hrbot.timers;

import com.fatemorgan.hrbot.model.settings.SettingsGlobalContainer;
import com.fatemorgan.hrbot.timers.tasks.EventsTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;

@Component
public class EventsTimer extends Timer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsTimer.class);

    private EventsTask eventsTask;

    public EventsTimer(EventsTask eventsTask){
        this.eventsTask = eventsTask;
    }

    public String start() {
        int eventsUpdateRate = SettingsGlobalContainer.getInstance().getTelegramSettings().getEventsUpdateRate();
        long seconds = eventsUpdateRate * 1000;
        this.scheduleAtFixedRate(eventsTask, 0, seconds);

        String message = String.format("EventsTimer has started with update rate: %s seconds", eventsUpdateRate);
        LOGGER.info(message);
        return message;
    }
}
