package com.fatemorgan.hrbot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.TimeZone;

@Component
public class Bootstrap implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);
    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Current timezone: {}", TimeZone.getDefault());
        LOGGER.info("Current locale: {}", Locale.getDefault());
    }
}
