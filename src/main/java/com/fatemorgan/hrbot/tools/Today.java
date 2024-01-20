package com.fatemorgan.hrbot.tools;


import com.fatemorgan.hrbot.model.settings.DateParser;

import java.time.*;
import java.util.Date;

public class Today {
    public static Date get() {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = new Date().toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date get(DateParser dateParser) {
        ZoneId zoneId = ZoneId.systemDefault();
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
