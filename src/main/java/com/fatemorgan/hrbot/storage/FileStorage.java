package com.fatemorgan.hrbot.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;

@Component
public class FileStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorage.class);
    private DateFormatter dateFormatter;

    public FileStorage(
            @Qualifier("storageDateFormatter") DateFormatter dateFormatter
    ) {
        this.dateFormatter = dateFormatter;
    }

    //TODO: yesterday messages file

    //TODO: today messages file

    public void write(){
        //TODO: JSON database?
    }

    public String read(){
        //TODO: JSON database?
        return null;
    }

    private void setFilePermission(){

    }

    private boolean isUnixSystem(){
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("nix") || osName.contains("nux");
    }
}
