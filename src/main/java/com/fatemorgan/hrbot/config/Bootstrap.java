package com.fatemorgan.hrbot.config;

import com.fatemorgan.hrbot.model.serializers.JsonMaker;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.model.settings.SettingsGlobalContainer;
import com.fatemorgan.hrbot.services.TimersService;
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

    private TimersService timersService;

    public Bootstrap(SettingsStorage settingsStorage,
                     TimersService timersService) {
        this.settingsStorage = settingsStorage;
        this.timersService = timersService;
    }

    @Override
    public void run(String... args) throws Exception {
        Settings settings = settingsStorage.getSettings();
        SettingsGlobalContainer.setInstance(settings);

        //TODO: check settings validity before running anything else

        ZoneId zoneId = settings.getZoneId();
        Locale locale = settings.getLocale();

        LOGGER.info("Current timezone: {}", zoneId);
        LOGGER.info("Current locale: {}", locale);
        LOGGER.info("Today is {}", new Date().toInstant().atZone(zoneId));

        timersService.startAllJobs();
    }
}
