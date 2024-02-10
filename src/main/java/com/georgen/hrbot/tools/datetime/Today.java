package com.georgen.hrbot.tools.datetime;


import com.georgen.hrbot.model.settings.DateParser;
import com.georgen.hrbot.model.settings.Settings;
import com.georgen.hrbot.model.settings.SettingsGlobalContainer;

import java.time.*;
import java.util.Date;

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
        Settings settings = SettingsGlobalContainer.getInstance();

        ZoneId zoneId = settings.getZoneId();
        Instant instant = new Date().toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        Date today = Date.from(zonedDateTime.toInstant());
        return dateParser.parse(today);
    }
}
