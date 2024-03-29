package com.georgen.hrbot.model.settings;

import com.georgen.hrbot.model.constants.DefaultDataSettings;
import com.georgen.hrbot.model.exceptions.DateParserException;
import com.georgen.hrbot.tools.LocaleParser;
import org.springframework.format.datetime.DateFormatter;

import java.util.Date;
import java.util.Locale;

public class DateParser {
    private DateFormatter dateFormatter;
    private Locale locale;

    public DateParser(){
        this.dateFormatter = new DateFormatter(DefaultDataSettings.DEFAULT_DATE_FORMAT);
        this.locale = LocaleParser.parse(DefaultDataSettings.DEFAULT_LOCALE);
    }

    public DateParser(DateFormatter dateFormatter, Locale locale) throws DateParserException {
        this.dateFormatter = dateFormatter;
        this.locale = locale;
        if (dateFormatter == null || locale == null) throw new DateParserException("DateParser has null constructor parameters");
    }

    public Date parse(String stringDate) {
        try {
            return dateFormatter.parse(stringDate.toUpperCase(), locale);
        } catch (Exception e){
            return new Date();
        }
    }

    public String parse(Date date){
        return dateFormatter.print(date, locale);
    }
}
