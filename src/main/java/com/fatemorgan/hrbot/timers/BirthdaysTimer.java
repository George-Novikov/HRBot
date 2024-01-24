package com.fatemorgan.hrbot.timers;

import com.fatemorgan.hrbot.timers.tasks.BirthdaysTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;

@Component
public class BirthdaysTimer extends Timer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatTimer.class);
    @Value("${telegram.timers.update-rate.birthdays}")
    private Integer birthdaysUpdateRate;

    private BirthdaysTask birthdaysTask;

    public BirthdaysTimer(BirthdaysTask birthdaysTask){
        this.birthdaysTask = birthdaysTask;
    }

    public void start() {
        long seconds = birthdaysUpdateRate * 1000;
        LOGGER.info("BirthdaysTimer has started with update rate: {} seconds", birthdaysUpdateRate);
        this.scheduleAtFixedRate(birthdaysTask, 0, seconds);
    }
}