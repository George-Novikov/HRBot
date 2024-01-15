package com.fatemorgan.hrbot.tools.comparators;

import com.fatemorgan.hrbot.model.events.Event;
import com.fatemorgan.hrbot.model.settings.DateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Date;

public class EventsDateComparator implements Comparator<Event> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsDateComparator.class);
    private DateParser dateParser;

    public EventsDateComparator(DateParser dateParser) {
        this.dateParser = dateParser;
    }

    @Override
    public int compare(Event event1, Event event2) {
        try {
            Date date1 = dateParser.parse(event1.getDate());
            Date date2 = dateParser.parse(event2.getDate());

            if (date1 == null || date2 == null) return 0;

            return date1.compareTo(date2);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return 0;
        }
    }
}
