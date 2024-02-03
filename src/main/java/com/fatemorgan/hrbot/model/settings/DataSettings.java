package com.fatemorgan.hrbot.model.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fatemorgan.hrbot.model.constants.ColumnName;
import com.fatemorgan.hrbot.model.constants.DataDefaults;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.serializers.JsonMaker;
import com.fatemorgan.hrbot.tools.LocaleParser;
import com.fatemorgan.hrbot.tools.SafeReader;
import org.springframework.format.datetime.DateFormatter;

import java.util.*;
import java.util.stream.IntStream;

@JsonIgnoreProperties(value = {"dateParser", "isHeader", "isEmpty"}, ignoreUnknown = true)
public class DataSettings {
    private String timeOffset;
    private String locale;
    private String dateFormat;
    private String birthdayGreeting;
    private String nextBirthdayRequest;
    private DateParser dateParser;
    private Map<String, String> columnNames;
    private Map<String, Integer> columnsOrder;

    public DataSettings(){
        this.columnNames = new HashMap();
        this.columnsOrder = new HashMap<>();
    };
    public DataSettings(SheetData sheet, List<SheetData> sheets){
        if (sheet.isEmpty()) return;

        this.columnNames = new HashMap();
        this.columnsOrder = new HashMap<>();

        fill(sheet.getRows());
        fillColumnsOrder(sheets);
    }

    public String getTimeOffset() {
        if (this.timeOffset == null) return DataDefaults.DEFAULT_TIME_OFFSET;
        return timeOffset;
    }

    public void setTimeOffset(String timeOffset) {
        this.timeOffset = timeOffset;
    }

    public String getLocale() {
        if (this.locale == null) return DataDefaults.DEFAULT_LOCALE;
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDateFormat() {
        if (this.dateFormat == null) return DataDefaults.DEFAULT_DATE_FORMAT;
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getBirthdayGreeting() {
        if (this.birthdayGreeting == null) return DataDefaults.DEFAULT_BIRTHDAY_GREETING;
        return birthdayGreeting;
    }

    public void setBirthdayGreeting(String birthdayGreeting) {
        this.birthdayGreeting = birthdayGreeting;
    }

    public String getNextBirthdayRequest() {
        if (this.nextBirthdayRequest == null) return DataDefaults.DEFAULT_NEXT_BIRTHDAY_REQUEST;
        return nextBirthdayRequest;
    }

    public void setNextBirthdayRequest(String nextBirthdayRequest) {
        this.nextBirthdayRequest = nextBirthdayRequest;
    }

    @JsonProperty("dateParser")
    public DateParser getDateParser() throws DateParserException {
        if (this.dateParser != null) return this.dateParser;

        if (this.locale == null) this.locale = DataDefaults.DEFAULT_LOCALE;
        if (this.dateFormat == null) this.dateFormat = DataDefaults.DEFAULT_DATE_FORMAT;

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

    private void fill(List<List<String>> rows){
        rows
                .stream()
                .forEach(row -> {
                    if (row.size() >= 2){
                        routeValueToField(row.get(0), row.get(1));
                    }
                });
    }

    private void routeValueToField(String key, String value){
        switch (key){
            case DataDefaults.LOCALE_FIELD:
                this.locale = value;
                break;
            case DataDefaults.DATE_FORMAT_FIELD:
                this.dateFormat = value;
                break;
            case DataDefaults.BIRTHDAY_GREETING_FIELD:
                this.birthdayGreeting = value;
                break;
            case DataDefaults.NEXT_BIRTHDAY_REQUEST_FIELD:
                this.nextBirthdayRequest = value;
                break;
            default:
                if (!value.isEmpty()){
                    columnNames.put(key, value);
                }
                break;
        }
    }

    private void fillColumnsOrder(List<SheetData> sheets){
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

    public void fillDefaultColumns(){
        Arrays
                .stream(ColumnName.values())
                .forEach(column -> {
                    if (columnNames == null || !columnNames.containsKey(column.name())){
                        this.columnNames.put(column.name(), "");
                    }
                });
    }

    @JsonProperty("isHeader")
    public boolean isHeader(String value){
        return this.columnNames.entrySet().stream().anyMatch(entry -> entry.getValue().equals(value));
    }

    public String toJson() throws JsonProcessingException {
        return JsonMaker.serialize(this);
    }

    @JsonProperty("isEmpty")
    public boolean isEmpty(){
        return this.locale == null || this.dateFormat == null || this.columnNames == null || this.columnsOrder == null;
    }
}
