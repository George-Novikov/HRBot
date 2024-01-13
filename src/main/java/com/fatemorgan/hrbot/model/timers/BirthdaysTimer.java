package com.fatemorgan.hrbot.model.timers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class BirthdaysTimer extends Timer {
    @Value("${telegram.timers.update-rate.birthdays}")
    private Integer birthdaysUpdateRate;

    public void start(TimerTask task) {

    }
}
