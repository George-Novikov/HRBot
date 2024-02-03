package com.fatemorgan.hrbot.model.settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class SettingsGlobalContainer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsGlobalContainer.class);
    private static Settings instance;

    public synchronized static Settings getInstance(){
        try {
            if (isEmpty()) setInstance(new Settings());
            return instance;
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return new Settings();
        }
    }

    public synchronized static void setInstance(Settings settings){
        try {
            if (isEmpty()) LOGGER.info("Global settings container initialized");
            instance = settings;
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static boolean isEmpty(){
        try {
            AtomicBoolean isEmpty = new AtomicBoolean(true);

            if (instance == null){
                synchronized (SettingsGlobalContainer.class){
                    isEmpty.set(instance == null);
                }
            } else {
                isEmpty.set(false);
            }

            return isEmpty.get();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }
}
