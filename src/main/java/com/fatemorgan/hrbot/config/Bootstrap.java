package com.fatemorgan.hrbot.config;

import com.fatemorgan.hrbot.model.serializers.JsonMaker;
import com.fatemorgan.hrbot.timers.BirthdaysTimer;
import com.fatemorgan.hrbot.timers.ChatTimer;
import com.fatemorgan.hrbot.timers.EventsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.TimeZone;

@Component
public class Bootstrap implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    private ChatTimer chatTimer;
    private BirthdaysTimer birthdaysTimer;
    private EventsTimer eventsTimer;

    public Bootstrap(ChatTimer chatTimer,
                     BirthdaysTimer birthdaysTimer,
                     EventsTimer eventsTimer) {
        this.chatTimer = chatTimer;
        this.birthdaysTimer = birthdaysTimer;
        this.eventsTimer = eventsTimer;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("System timezone: {}", TimeZone.getDefault());
        LOGGER.info("System locale: {}", Locale.getDefault());

        //TODO: check settings validity before running timers

        chatTimer.start();
        birthdaysTimer.start();
        eventsTimer.start();
    }
}
