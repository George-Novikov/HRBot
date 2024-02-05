package com.fatemorgan.hrbot.timers;

import com.fatemorgan.hrbot.model.settings.SettingsGlobalContainer;
import com.fatemorgan.hrbot.timers.tasks.BirthdaysTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Timer;

@Component
public class BirthdaysTimer extends Timer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatTimer.class);

    private BirthdaysTask birthdaysTask;

    public BirthdaysTimer(BirthdaysTask birthdaysTask){
        this.birthdaysTask = birthdaysTask;
    }

    public String start() {
        int birthdaysUpdateRate = SettingsGlobalContainer.getInstance().getTelegramSettings().getBirthdaysUpdateRate();
        long seconds = birthdaysUpdateRate * 1000;
        this.scheduleAtFixedRate(birthdaysTask, 0, seconds);

        String message = String.format("BirthdaysTimer has started with update rate: %s seconds", birthdaysUpdateRate);
        LOGGER.info(message);
        return message;
    }
}
