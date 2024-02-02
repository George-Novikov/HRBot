package com.fatemorgan.hrbot.model.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fatemorgan.hrbot.model.constants.SettingsAttribute;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.serializers.JsonMaker;
import com.fatemorgan.hrbot.tools.LocaleParser;
import com.fatemorgan.hrbot.tools.SafeReader;
import org.springframework.format.datetime.DateFormatter;

import java.util.*;
import java.util.stream.IntStream;

@JsonIgnoreProperties(value = {"dateParser"})
public class DataSettings {
    private String locale;
    private String dateFormat;
    private String birthdayGreeting;
    private String nextBirthdayRequest;
    private DateParser dateParser;
    private Map<String, String> columns;
    private Map<String, Integer> columnsOrder;

    public DataSettings(SheetData sheet, List<SheetData> sheets){
        if (sheet.isEmpty()) return;

        this.columns = new HashMap();
        this.columnsOrder = new HashMap<>();

        fill(sheet.getRows());
        fillColumnsOrder(sheets);
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getBirthdayGreeting() {
        return birthdayGreeting;
    }

    public void setBirthdayGreeting(String birthdayGreeting) {
        this.birthdayGreeting = birthdayGreeting;
    }

    public String getNextBirthdayRequest() {
        return nextBirthdayRequest;
    }

    public void setNextBirthdayRequest(String nextBirthdayRequest) {
        this.nextBirthdayRequest = nextBirthdayRequest;
    }

    @JsonProperty("dateParser")
    public DateParser getDateParser() throws DateParserException {
        if (this.dateParser != null) return this.dateParser;

        if (this.locale == null) this.locale = SettingsAttribute.DEFAULT_LOCALE;
        if (this.dateFormat == null) this.dateFormat = SettingsAttribute.DEFAULT_DATE_FORMAT;

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

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, String> columns) {
        this.columns = columns;
    }

    public Map<String, Integer> getColumnsOrder() {
        return columnsOrder;
    }

    public void setColumnsOrder(Map<String, Integer> columnsOrder) {
        this.columnsOrder = columnsOrder;
    }

    public String getColumn(String name){
        return this.columns != null ? columns.get(name) : null;
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
            case SettingsAttribute.LOCALE:
                this.locale = value;
                break;
            case SettingsAttribute.DATE_FORMAT:
                this.dateFormat = value;
                break;
            case SettingsAttribute.BIRTHDAY_GREETING:
                this.birthdayGreeting = value;
                break;
            case SettingsAttribute.NEXT_BIRTHDAY_REQUEST:
                this.nextBirthdayRequest = value;
                break;
            default:
                if (!value.isEmpty()){
                    columns.put(key, value);
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

    public boolean isHeader(String value){
        return this.columns.entrySet().stream().anyMatch(entry -> entry.getValue().equals(value));
    }

    public String toJson() throws JsonProcessingException {
        return JsonMaker.serialize(this);
    }

    public boolean isEmpty(){
        return this.locale == null || this.dateFormat == null || this.columns == null || this.columnsOrder == null;
    }
}
