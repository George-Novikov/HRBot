package com.georgen.hrbot.tools;

import com.georgen.hrbot.model.settings.DateParser;

import java.util.Collection;
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

    public static <T> boolean isValid(Collection<T> collection){
        return collection != null && !collection.isEmpty();
    }

    public static boolean isBlank(String value){
        return value.chars().allMatch(c -> c == 32);
    }

    public static boolean isValid(String value){
        return value != null && !value.isEmpty() && !isBlank(value);
    }
}
