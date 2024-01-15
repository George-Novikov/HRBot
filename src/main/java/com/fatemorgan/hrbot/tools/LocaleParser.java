package com.fatemorgan.hrbot.tools;

import com.fatemorgan.hrbot.model.constants.SettingsAttribute;

import java.util.Locale;

public class LocaleParser {
    public static Locale parse(String locale){
        if (locale == null) locale = SettingsAttribute.DEFAULT_LOCALE;

        String[] localeParts = locale.split("_");
        if (localeParts.length == 1) localeParts[1] = "";

        return new Locale(localeParts[0], localeParts[1]);
    }
}