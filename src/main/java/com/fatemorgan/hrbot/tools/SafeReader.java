package com.fatemorgan.hrbot.tools;

import com.fatemorgan.hrbot.model.settings.DateParser;
import org.springframework.format.datetime.DateFormatter;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class SafeReader {
    public static String getValue(List<String> list, int index){
        return index < list.size() && index >= 0 ? list.get(index) : "";
    }

    public static Date parseDate(DateParser dateParser, String dateString){
        if (dateParser == null || dateString == null || dateString.isEmpty()) return null;

        try {
            return dateParser.parse(dateString);
        } catch (Exception e){
            return null;
        }
    }
}
