package com.georgen.hrbot.timers.tasks;

import com.georgen.hrbot.model.constants.BirthdaysMessage;
import com.georgen.hrbot.model.settings.Settings;
import com.georgen.hrbot.model.settings.SettingsGlobalContainer;
import com.georgen.hrbot.services.TelegramBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class BirthdaysTask extends TimerTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(BirthdaysTask.class);
    private TelegramBotService bot;

    public BirthdaysTask(TelegramBotService bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        try {
            Settings settings = SettingsGlobalContainer.getInstance();

            if (!settings.isWorkingTime()){
                LOGGER.info(BirthdaysMessage.BIRTHDAYS_TASK_DOES_NOTHING.describe());
                return;
            }

            bot.processCurrentBirthdays();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }
}
