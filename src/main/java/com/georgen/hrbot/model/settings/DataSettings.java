package com.georgen.hrbot.model.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.hrbot.model.constants.ColumnName;
import com.georgen.hrbot.model.constants.DefaultDataSettings;
import com.georgen.hrbot.model.exceptions.DateParserException;
import com.georgen.hrbot.model.google.SheetData;
import com.georgen.hrbot.model.serializers.JsonMaker;
import com.georgen.hrbot.tools.LocaleParser;
import com.georgen.hrbot.tools.SafeReader;
import org.springframework.format.datetime.DateFormatter;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.IntStream;

@JsonIgnoreProperties(value = {"dateParser", "isHeader", "isEmpty"}, ignoreUnknown = true)
public class DataSettings {
    private String timeOffset;
    private String locale;
    private String dateFormat;
    private String workingTimeRange;
    private String birthdayGreeting;
    private String nextBirthdayRequest;
    private DateParser dateParser;
    private Map<String, String> columnNames;
    private Map<String, Integer> columnsOrder;

    public DataSettings(){
        this.columnNames = new HashMap();
        this.columnsOrder = new HashMap<>();
    };

    public String getTimeOffset() {
        if (this.timeOffset == null) return DefaultDataSettings.DEFAULT_TIME_OFFSET;
        return timeOffset;
    }

    public void setTimeOffset(String timeOffset) {
        this.timeOffset = timeOffset;
    }

    public String getLocale() {
        if (this.locale == null) return DefaultDataSettings.DEFAULT_LOCALE;
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDateFormat() {
        if (this.dateFormat == null) return DefaultDataSettings.DEFAULT_DATE_FORMAT;
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getWorkingTimeRange() {
        if (this.workingTimeRange == null) return DefaultDataSettings.DEFAULT_WORKING_TIME_RANGE;
        return workingTimeRange;
    }

    public void setWorkingTimeRange(String workingTimeRange) {
        this.workingTimeRange = workingTimeRange;
    }

    public String getBirthdayGreeting() {
        if (this.birthdayGreeting == null) return DefaultDataSettings.DEFAULT_BIRTHDAY_GREETING;
        return birthdayGreeting;
    }

    public void setBirthdayGreeting(String birthdayGreeting) {
        this.birthdayGreeting = birthdayGreeting;
    }

    public String getNextBirthdayRequest() {
        if (this.nextBirthdayRequest == null) return DefaultDataSettings.DEFAULT_NEXT_BIRTHDAY_REQUEST;
        return nextBirthdayRequest;
    }

    public void setNextBirthdayRequest(String nextBirthdayRequest) {
        this.nextBirthdayRequest = nextBirthdayRequest;
    }

    @JsonProperty("dateParser")
    public DateParser getDateParser() throws DateParserException {
        if (this.dateParser != null) return this.dateParser;

        if (this.locale == null) this.locale = DefaultDataSettings.DEFAULT_LOCALE;
        if (this.dateFormat == null) this.dateFormat = DefaultDataSettings.DEFAULT_DATE_FORMAT;

        this.dateParser = new DateParser(
                new DateFormatter(this.dateFormat),
                LocaleParser.parse(this.locale)
        );

        return this.dateParser;
    }

    public Date parseDate(String dateString) throws DateParserException {
        DateParser dateParser = getDateParser();
        return SafeReader.parseDate(dateParser, dateString);
    }

    public Map<String, String> getColumnNames() {
        if (columnNames == null || columnNames.size() < ColumnName.values().length){
            fillDefaultColumns();
        }
        return columnNames;
    }

    public void setColumnNames(Map<String, String> columnNames) {
        this.columnNames = columnNames;
    }

    public Map<String, Integer> getColumnsOrder() {
        if (this.columnsOrder == null) this.columnsOrder = new HashMap<>();
        return columnsOrder;
    }

    public void setColumnsOrder(Map<String, Integer> columnsOrder) {
        this.columnsOrder = columnsOrder;
    }

    public String getColumn(String name){
        return this.columnNames != null ? columnNames.get(name) : null;
    }

    public Integer getColumnIndex(String name){
        if (this.columnsOrder == null) return null;
        String columnName = getColumn(name);
        return columnName != null ? this.columnsOrder.get(columnName) : null;
    }
    public void fillColumnsOrder(List<SheetData> sheets){
        for (SheetData sheet : sheets){
            if (sheet.isEmpty()) continue;

            List<String> headerRow = sheet.getRows().get(0);

            IntStream
                    .range(0, headerRow.size())
                    .forEach(index -> {
                        this.columnsOrder.put(
                                headerRow.get(index),
                                index
                        );
                    });
        }
    }

    private void fillDefaultColumns(){
        Arrays
                .stream(ColumnName.values())
                .forEach(column -> {
                    if (columnNames == null || !columnNames.containsKey(column.name())){
                        this.columnNames.put(column.name(), "");
                    }
                });
    }

    public String toJson() throws JsonProcessingException {
        return JsonMaker.serialize(this);
    }

    public boolean isWorkingTime(){
        try {
            String[] timeRange = this.getWorkingTimeRange().split("-");
            if (timeRange.length < 2) return false;

            LocalTime start = LocalTime.parse(timeRange[0]);
            LocalTime end = LocalTime.parse(timeRange[1]);

            return LocalTime.now().isAfter(start) && LocalTime.now().isBefore(end);
        } catch (Exception e){
            return false;
        }
    }

    @JsonProperty("isHeader")
    public boolean isHeader(String value){
        return this.columnNames.entrySet().stream().anyMatch(entry -> entry.getValue().equals(value));
    }

    @JsonProperty("isEmpty")
    public boolean isEmpty(){
        return this.getLocale() == null || this.getDateFormat() == null || this.getColumnNames() == null || this.getColumnsOrder() == null;
    }
}
