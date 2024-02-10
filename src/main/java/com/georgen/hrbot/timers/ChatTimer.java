package com.georgen.hrbot.timers;

import com.georgen.hrbot.model.settings.SettingsGlobalContainer;
import com.georgen.hrbot.timers.tasks.ChatTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Timer;

@Component
public class ChatTimer extends Timer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatTimer.class);

    private ChatTask chatTask;

    public ChatTimer(ChatTask chatTask){
        this.chatTask = chatTask;
    }

    public String start(){
        int chatUpdateRate = SettingsGlobalContainer.getInstance().getTelegramSettings().getChatUpdateRate();
        long seconds = chatUpdateRate * 1000;
        this.scheduleAtFixedRate(chatTask, 0, seconds);

        String message = String.format("ChatTimer has started with update rate: %s seconds", chatUpdateRate);
        LOGGER.info(message);
        return message;
    }
}
