package com.fatemorgan.hrbot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.settings.DateParser;
import com.fatemorgan.hrbot.storage.FileManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Configuration
public class StorageConfig {
    private static final String MANAGING_FILE_NAME = "storage.json";
    private static final String EVENTS_FILE_NAME = "events.json";
    private static final String SETTINGS_FILE_NAME = "settings.json";
    private static final String TIMESTAMP_PATTERN = "yyyyMMdd";

    @Bean
    @Qualifier("storageDateParser")
    public DateParser storageDateParser() throws DateParserException {
        return new DateParser(
                new DateFormatter(TIMESTAMP_PATTERN),
                Locale.getDefault()
        );
    }

    @Bean
    @Qualifier("managementFile")
    public File getManagementFile() throws IOException {
        File managementFile = new File(MANAGING_FILE_NAME);
        FileManager.createOrBypass(managementFile);
        return managementFile;
    }

    @Bean
    @Qualifier("eventsFile")
    public File getEventsFile() throws IOException {
        File eventsFile = new File(EVENTS_FILE_NAME);
        FileManager.createOrBypass(eventsFile);
        return eventsFile;
    }

    @Bean
    @Qualifier("settingsFile")
    public File getSettingsFile() throws IOException {
        File settingsFile = new File(SETTINGS_FILE_NAME);
        FileManager.createOrBypass(settingsFile);
        return settingsFile;
    }

    @Bean
    @Qualifier("settingsObjectMapper")
    public ObjectMapper settingsObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
