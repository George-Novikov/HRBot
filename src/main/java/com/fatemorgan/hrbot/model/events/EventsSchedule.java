package com.fatemorgan.hrbot.model.events;

import com.fatemorgan.hrbot.model.constants.ColumnName;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.settings.DataSettings;
import com.fatemorgan.hrbot.model.settings.DateParser;
import com.fatemorgan.hrbot.model.settings.SettingsGlobalContainer;
import com.fatemorgan.hrbot.tools.SafeReader;
import com.fatemorgan.hrbot.tools.datetime.Today;
import com.fatemorgan.hrbot.tools.comparators.EventsDateComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventsSchedule {
    private DateParser dateParser = SettingsGlobalContainer.getInstance().getDateParser();
    private List<Event> events;
    public EventsSchedule(SheetData sheet, DataSettings dataSettings){
        this.events = new ArrayList<>();
        fillEvents(sheet, dataSettings);
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Event> findTodayEvents(DateParser dateParser){
        if (this.events.isEmpty()) return this.events;
        Date today = Today.get();
        return filterByEqualDate(today);
    }

    public List<Event> findNextEvents(DateParser dateParser){
        if (this.events.isEmpty()) return this.events;
        Date today = Today.get();
        List<Event> nextEvents = filterByNearestDate(today);
        return nextEvents.isEmpty() ? nextEvents : findMostRelevant(nextEvents);
    }

    public boolean isEmpty(){
        return this.events == null || this.events.isEmpty();
    }

    private List<Event> filterByEqualDate(Date date){
        return this.events
                .stream()
                .filter(event -> {
                    Date eventDate = dateParser.parse(event.getDate());
                    return date.equals(eventDate);
                })
                .collect(Collectors.toList());
    }
    private List<Event> filterByNearestDate(Date date){
        return this.events
                .stream()
                .filter(event -> {
                    Date eventDate = dateParser.parse(event.getDate());
                    return date.before(eventDate);
                })
                .collect(Collectors.toList());
    }

    private List<Event> findMostRelevant(List<Event> nextEvents){
        if (nextEvents == null || nextEvents.isEmpty()) return new ArrayList<>();
        Collections.sort(nextEvents, new EventsDateComparator(dateParser));
        Event nearest = nextEvents.get(0);
        return nextEvents
                .stream()
                .filter(event -> nearest.isEqualDate(event))
                .collect(Collectors.toList());
    }

    private void fillEvents(SheetData sheet, DataSettings dataSettings){
        if (sheet.isEmpty() || dataSettings.isEmpty()) return;

        int dateIndex = dataSettings.getColumnIndex(ColumnName.EVENT_DATE.name());
        int announcementIndex = dataSettings.getColumnIndex(ColumnName.ANNOUNCEMENT.name());

        //TODO: null check

        for (List<String> row : sheet.getRows()){
            if (row.isEmpty()) continue;

            String date = getSafeValue(row, dateIndex);
            String announcement =  getSafeValue(row, announcementIndex);

            if (dataSettings.isHeader(date) || dataSettings.isHeader(announcement)) continue;

            events.add(new Event(date, announcement));
        }
    }

    private String getSafeValue(List<String> row, int index){
        return SafeReader.getValue(row, index);
    }
}
