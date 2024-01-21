package com.fatemorgan.hrbot.config;

import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.settings.DateParser;
import com.fatemorgan.hrbot.storage.FileManager;
import com.fatemorgan.hrbot.tools.Today;
import com.fatemorgan.hrbot.tools.Yesterday;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class StorageConfig {
    private static final String MANAGING_FILE_NAME = "storage.json";
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
    public File getManagementFile(@Qualifier("storageDateParser") DateParser dateParser) throws IOException {
        File managementFile = new File(MANAGING_FILE_NAME);
        FileManager.createOrBypass(managementFile);
        return managementFile;
    }
}
