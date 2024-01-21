package com.fatemorgan.hrbot.timers;

import com.fatemorgan.hrbot.timers.tasks.ChatTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;

@Component
public class ChatTimer extends Timer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatTimer.class);
    @Value("${telegram.timers.update-rate.chat}")
    private Integer chatUpdateRate;

    private ChatTask chatTask;

    public ChatTimer(ChatTask chatTask){
        this.chatTask = chatTask;
    }

    public void start(){
        long seconds = chatUpdateRate * 1000;
        LOGGER.info("ChatTimer has started with update rate: {} seconds", chatUpdateRate);
        this.scheduleAtFixedRate(chatTask, 0, seconds);
    }
}
