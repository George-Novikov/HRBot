package com.fatemorgan.hrbot.tools.datetime;

import com.fatemorgan.hrbot.model.settings.DateParser;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.model.settings.SettingsGlobalContainer;
import org.springframework.format.datetime.DateFormatter;

import javax.print.attribute.SetOfIntegerSyntax;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

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
