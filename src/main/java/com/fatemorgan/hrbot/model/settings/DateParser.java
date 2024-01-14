package com.fatemorgan.hrbot.model.settings;

import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import org.springframework.format.datetime.DateFormatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class DateParser {
    private DateFormatter dateFormatter;
    private Locale locale;

    public DateParser(DateFormatter dateFormatter, Locale locale) throws DateParserException {
        this.dateFormatter = dateFormatter;
        this.locale = locale;
        if (dateFormatter == null || locale == null) throw new DateParserException("DateParser has null constructor parameters");
    }

    public Date parse(String stringDate) {
        try {
            return dateFormatter.parse(stringDate, locale);
        } catch (Exception e){
            return new Date();
        }
    }

    public String parse(Date date){
        return dateFormatter.print(date, locale);
    }
}
