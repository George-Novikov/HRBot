package com.fatemorgan.hrbot.storage;

import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.settings.DateParser;
import com.fatemorgan.hrbot.tools.Today;
import com.fatemorgan.hrbot.tools.Yesterday;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class StorageConfig {
    @Value("${storage.managing-file}")
    private String managingFileName;
    @Value("${storage.timestamp-format}")
    private String timestampFormat;

    @Bean
    @Qualifier("storageDateParser")
    public DateParser storageDateParser() throws DateParserException {
        return new DateParser(
                new DateFormatter(timestampFormat),
                Locale.getDefault()
        );
    }

    @Bean
    @Qualifier("managingFile")
    public File getManagingFile(@Qualifier("storageDateParser") DateParser dateParser) throws IOException {
        File yesterdayFile = new File(managingFileName);
        if (isUnixSystem()) setFilePermissions(yesterdayFile);
        return yesterdayFile;
    }

    @Bean
    @Qualifier("yesterdayFile")
    public File getYesterdayFile(@Qualifier("storageDateParser") DateParser dateParser) throws IOException {
        String fileName = String.format("%s.json", Yesterday.getString(dateParser));
        File yesterdayFile = new File(fileName);
        if (isUnixSystem()) setFilePermissions(yesterdayFile);
        return yesterdayFile;
    }

    @Bean
    @Qualifier("todayFile")
    public File getTodayFile(@Qualifier("storageDateParser") DateParser dateParser) throws IOException {
        String fileName = String.format("%s.json", Today.getString(dateParser));
        File yesterdayFile = new File(fileName);
        if (isUnixSystem()) setFilePermissions(yesterdayFile);
        return yesterdayFile;
    }

    private void setFilePermissions(File file) throws IOException {
        Set<PosixFilePermission> permissions = Arrays.stream(PosixFilePermission.values()).collect(Collectors.toSet());
        Files.setPosixFilePermissions(file.toPath(), permissions);
    }

    private boolean isUnixSystem(){
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("nix") || osName.contains("nux");
    }
}
