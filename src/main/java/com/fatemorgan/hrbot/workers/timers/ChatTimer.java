package com.fatemorgan.hrbot.workers.timers;

import com.fatemorgan.hrbot.workers.timers.tasks.ChatTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class ChatTimer extends Timer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatTimer.class);
    @Value("${telegram.timers.update-rate.chat}")
    private Integer updateRate;

    private ChatTask chatTask;

    public ChatTimer(ChatTask chatTask){
        this.chatTask = chatTask;
    }

    public void start(){
        LOGGER.info("ChatTimer has started with update rate: {} seconds", updateRate);
        this.scheduleAtFixedRate(chatTask, updateRate, updateRate);
    }
}
