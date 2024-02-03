package com.fatemorgan.hrbot.model.settings;

import com.fatemorgan.hrbot.model.constants.DataDefaults;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.tools.LocaleParser;
import org.springframework.format.datetime.DateFormatter;

import java.util.Date;
import java.util.Locale;

public class DateParser {
    private DateFormatter dateFormatter;
    private Locale locale;

    public DateParser(){
        this.dateFormatter = new DateFormatter(DataDefaults.DEFAULT_DATE_FORMAT);
        this.locale = LocaleParser.parse(DataDefaults.DEFAULT_LOCALE);
    }

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
