package com.georgen.hrbot.storage;

import com.georgen.hrbot.model.serializers.SettingsSerializer;
import com.georgen.hrbot.model.settings.Settings;
import com.georgen.hrbot.model.settings.SettingsGlobalContainer;
import com.georgen.hrbot.tools.SafeReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

@Component
public class SettingsStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsStorage.class);
    private File settingsFile;
    private SettingsSerializer serializer;

    public SettingsStorage(@Qualifier("settingsFile") File settingsFile,
                           SettingsSerializer serializer) {
        this.settingsFile = settingsFile;
        this.serializer = serializer;
    }

    public void saveSettings(Settings settings){
        if (settings == null) return;

        try {
            Map<String, Integer> columnsOrder = getSettings().getColumnsOrder();

            SettingsGlobalContainer.setInstance(settings);
            String settingsJson = serializer.serialize(settings);
            FileManager.write(settingsFile, settingsJson);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }

    public Settings getSettings(){
        try {
            String settingsJson = FileManager.read(settingsFile);
            if (!SafeReader.isValid(settingsJson)) return initDefaultSettings();
            return serializer.deserialize(settingsJson);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return initDefaultSettings();
        }
    }

    public Settings initDefaultSettings(){
        Settings settings = new Settings();
        saveSettings(settings);
        return settings;
    }
}
