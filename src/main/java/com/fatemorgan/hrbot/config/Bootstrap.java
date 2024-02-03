package com.fatemorgan.hrbot.config;

import com.fatemorgan.hrbot.model.serializers.JsonMaker;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.model.settings.SettingsGlobalContainer;
import com.fatemorgan.hrbot.storage.SettingsStorage;
import com.fatemorgan.hrbot.timers.BirthdaysTimer;
import com.fatemorgan.hrbot.timers.ChatTimer;
import com.fatemorgan.hrbot.timers.EventsTimer;
import com.fatemorgan.hrbot.tools.datetime.Today;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class Bootstrap implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    private SettingsStorage settingsStorage;

    private ChatTimer chatTimer;
    private BirthdaysTimer birthdaysTimer;
    private EventsTimer eventsTimer;

    public Bootstrap(SettingsStorage settingsStorage,
                     ChatTimer chatTimer,
                     BirthdaysTimer birthdaysTimer,
                     EventsTimer eventsTimer) {
        this.settingsStorage = settingsStorage;
        this.chatTimer = chatTimer;
        this.birthdaysTimer = birthdaysTimer;
        this.eventsTimer = eventsTimer;
    }

    @Override
    public void run(String... args) throws Exception {
        Settings settings = settingsStorage.getSettings();
        SettingsGlobalContainer.setInstance(settings);

        LOGGER.info("Current timezone: {}", settings.getZoneId());
        LOGGER.info("Current locale: {}", settings.getLocale());
        LOGGER.info("Today is {}", new Date().toInstant().atZone(settings.getZoneId()));

        //TODO: check settings validity before running timers

        chatTimer.start();
        birthdaysTimer.start();
        eventsTimer.start();
    }
}
