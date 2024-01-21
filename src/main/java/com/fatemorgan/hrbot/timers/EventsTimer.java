package com.fatemorgan.hrbot.timers;

import com.fatemorgan.hrbot.timers.tasks.EventsTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class EventsTimer extends Timer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsTimer.class);
    @Value("${telegram.timers.update-rate.events}")
    private Integer updateRate;

    private EventsTask eventsTask;

    public EventsTimer(EventsTask eventsTask){
        this.eventsTask = eventsTask;
    }

    public void start() {
        long delay = 5 * 1000;
        long seconds = updateRate * 1000;
        LOGGER.info("EventsTimer has started with update rate: {} seconds", updateRate);
        this.scheduleAtFixedRate(eventsTask, delay, seconds);
    }
}
