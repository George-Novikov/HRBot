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

    public void start() {
        int eventsUpdateRate = SettingsGlobalContainer.getInstance().getTelegramSettings().getEventsUpdateRate();
        long delay = 5 * 1000;
        long seconds = eventsUpdateRate * 1000;
        LOGGER.info("EventsTimer has started with update rate: {} seconds", eventsUpdateRate);
        this.scheduleAtFixedRate(eventsTask, delay, seconds);
    }
}
