package com.fatemorgan.hrbot.model.timers;

import com.fatemorgan.hrbot.model.timers.tasks.ChatTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class ChatTimer extends Timer {
    @Value("${telegram.timers.update-rate.chat}")
    private Integer updateRate;

    public void start(TimerTask task){

    }
}
