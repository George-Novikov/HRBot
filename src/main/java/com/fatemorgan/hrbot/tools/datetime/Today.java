package com.fatemorgan.hrbot.tools.datetime;


import com.fatemorgan.hrbot.model.settings.DateParser;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.model.settings.SettingsGlobalContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.Date;
import java.util.TimeZone;

public class Today {
    public static Date get() {
        Settings settings = SettingsGlobalContainer.getInstance();
        DateParser dateParser = settings.getDateParser();

        ZoneId zoneId = settings.getZoneId();
        Instant instant = new Date().toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        Date today = Date.from(zonedDateTime.toInstant());
        String stringBuffer = dateParser.parse(today);
        return dateParser.parse(stringBuffer);
    }

    public static String getString(DateParser dateParser) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = new Date().toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        Date today = Date.from(zonedDateTime.toInstant());
        return dateParser.parse(today);
    }
}
