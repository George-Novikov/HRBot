package com.fatemorgan.hrbot.workers.timers;

import com.fatemorgan.hrbot.workers.timers.tasks.BirthdaysTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class BirthdaysTimer extends Timer {
    @Value("${telegram.timers.update-rate.birthdays}")
    private Integer birthdaysUpdateRate;

    private BirthdaysTask birthdaysTask;

    public BirthdaysTimer(BirthdaysTask birthdaysTask){
        this.birthdaysTask = birthdaysTask;
    }

    public void start() {
        this.scheduleAtFixedRate(birthdaysTask, birthdaysUpdateRate, birthdaysUpdateRate);
    }
}
