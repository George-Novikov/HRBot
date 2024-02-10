package com.georgen.hrbot.tools.datetime;

import com.georgen.hrbot.model.settings.DateParser;
import com.georgen.hrbot.model.settings.Settings;
import com.georgen.hrbot.model.settings.SettingsGlobalContainer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Yesterday {
    public static Date get() {
        Settings settings = SettingsGlobalContainer.getInstance();
        DateParser dateParser = settings.getDateParser();

        ZoneId zoneId = settings.getZoneId();
        Instant todayInstant = new Date().toInstant();
        Instant yesterdayInstant = todayInstant.minus(1, ChronoUnit.DAYS);
        ZonedDateTime zonedDateTime = yesterdayInstant.atZone(zoneId);

        Date yesterday = Date.from(zonedDateTime.toInstant());
        String stringBuffer = dateParser.parse(yesterday);
        return dateParser.parse(stringBuffer);
    }

    public static String getString(DateParser dateParser) {
        Settings settings = SettingsGlobalContainer.getInstance();

        ZoneId zoneId = settings.getZoneId();
        Instant todayInstant = new Date().toInstant();
        Instant yesterdayInstant = todayInstant.minus(1, ChronoUnit.DAYS);
        ZonedDateTime zonedDateTime = yesterdayInstant.atZone(zoneId);

        Date yesterday = Date.from(zonedDateTime.toInstant());
        return dateParser.parse(yesterday);
    }
}
