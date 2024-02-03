package com.fatemorgan.hrbot.model.constants;

public class DataDefaults {
    public static final String DEFAULT_TIME_OFFSET = "UTC";
    public static final String DEFAULT_LOCALE = "en_US";
    public static final String DEFAULT_DATE_FORMAT = "dd MMMM";
    public static final String DEFAULT_BIRTHDAY_GREETING = String.format("Happy birthday, %s!", Placeholder.NAME);
    public static final String DEFAULT_NEXT_BIRTHDAY_REQUEST = "What is the next birthday?";

    public static final String LOCALE_FIELD = "LOCALE";
    public static final String DATE_FORMAT_FIELD = "DATE_FORMAT";
    public static final String BIRTHDAY_GREETING_FIELD = "BIRTHDAY_GREETING";
    public static final String NEXT_BIRTHDAY_REQUEST_FIELD = "NEXT_BIRTHDAY_REQUEST";
}
