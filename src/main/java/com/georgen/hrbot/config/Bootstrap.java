package com.georgen.hrbot.config;

import com.georgen.hrbot.model.constants.SystemMessage;
import com.georgen.hrbot.model.settings.Settings;
import com.georgen.hrbot.model.settings.SettingsGlobalContainer;
import com.georgen.hrbot.services.TimersService;
import com.georgen.hrbot.storage.SettingsStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

@DependsOn("initGlobalSettings")
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
        Settings settings = SettingsGlobalContainer.getInstance();

        //TODO: check settings validity before running anything else

        ZoneId zoneId = settings.getZoneId();
        Locale locale = settings.getLocale();
        boolean isReadyToWork = settings.isReadyToWork();

        LOGGER.info("Current timezone: {}", zoneId);
        LOGGER.info("Current locale: {}", locale);
        LOGGER.info("Today is {}", new Date().toInstant().atZone(zoneId));
        if (!isReadyToWork) LOGGER.warn(SystemMessage.CHECK_EMPTY_SETTING.describe());

        if (isReadyToWork){
            timersService.startAllJobs();
        } else {
            LOGGER.warn(SystemMessage.BOT_IS_NOT_READY.describe());
        }
    }
}
