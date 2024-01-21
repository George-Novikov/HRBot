package com.fatemorgan.hrbot.timers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class EventsTimer extends Timer {
    @Value("${telegram.timers.update-rate.events}")
    private Integer updateRate;

    public void start(TimerTask task) {

    }
}
