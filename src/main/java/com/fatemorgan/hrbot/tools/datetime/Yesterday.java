package com.fatemorgan.hrbot.tools.datetime;

import com.fatemorgan.hrbot.model.settings.DateParser;
import org.springframework.format.datetime.DateFormatter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class Yesterday {
    public static Date get() {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant todayInstant = new Date().toInstant();
        Instant yesterdayInstant = todayInstant.minus(1, ChronoUnit.DAYS);
        ZonedDateTime zonedDateTime = yesterdayInstant.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date get(DateParser dateParser) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant todayInstant = new Date().toInstant();
        Instant yesterdayInstant = todayInstant.minus(1, ChronoUnit.DAYS);
        ZonedDateTime zonedDateTime = yesterdayInstant.atZone(zoneId);

        Date yesterday = Date.from(zonedDateTime.toInstant());
        String stringBuffer = dateParser.parse(yesterday);
        return dateParser.parse(stringBuffer);
    }

    public static String getString(DateParser dateParser) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant todayInstant = new Date().toInstant();
        Instant yesterdayInstant = todayInstant.minus(1, ChronoUnit.DAYS);
        ZonedDateTime zonedDateTime = yesterdayInstant.atZone(zoneId);

        Date yesterday = Date.from(zonedDateTime.toInstant());
        return dateParser.parse(yesterday);
    }
}
