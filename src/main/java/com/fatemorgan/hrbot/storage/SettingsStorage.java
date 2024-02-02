package com.fatemorgan.hrbot.storage;

import com.fatemorgan.hrbot.model.serializers.SettingsSerializer;
import com.fatemorgan.hrbot.model.serializers.StringSetSerializer;
import com.fatemorgan.hrbot.model.settings.DateParser;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.tools.datetime.Today;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class SettingsStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsStorage.class);
    private File settingsFile;

    public SettingsStorage(@Qualifier("settingsFile") File settingsFile) {
        this.settingsFile = settingsFile;
    }

    public void saveSettings(Settings settings){
        if (settings == null) return;

        try {
            String settingsJson = SettingsSerializer.serialize(settings);
            FileManager.write(settingsFile, settingsJson);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }

    public Settings getSettings(){
        try {
            String settingsJson = FileManager.read(settingsFile);
            return SettingsSerializer.deserialize(settingsJson);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}
