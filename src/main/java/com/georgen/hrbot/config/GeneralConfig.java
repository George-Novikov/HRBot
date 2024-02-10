package com.georgen.hrbot.config;

import com.georgen.hrbot.model.DataGlobalContainer;
import com.georgen.hrbot.model.settings.Settings;
import com.georgen.hrbot.model.settings.SettingsGlobalContainer;
import com.georgen.hrbot.storage.SettingsStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static com.georgen.hrbot.model.constants.SystemMessage.SETTINGS_HAVE_LOADED;

@Configuration
public class GeneralConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralConfig.class);
    private SettingsStorage settingsStorage;

    public GeneralConfig(SettingsStorage settingsStorage) {
        this.settingsStorage = settingsStorage;
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public DataGlobalContainer globalDataContainer(){
        return new DataGlobalContainer();
    }

    @Bean("initGlobalSettings")
    public void initGlobalSettings(){
        Settings settings = settingsStorage.getSettings();
        SettingsGlobalContainer.setInstance(settings);
        LOGGER.info(SETTINGS_HAVE_LOADED.describe());
    }

    public static boolean isUnixSystem(){
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("nix") || osName.contains("nux");
    }
}
