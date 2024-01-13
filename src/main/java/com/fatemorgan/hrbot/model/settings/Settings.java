package com.fatemorgan.hrbot.model.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fatemorgan.hrbot.model.constants.SettingsAttribute;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.serializers.JsonMaker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

@JsonIgnoreProperties(value = {"dateParser"})
public class Settings {
    private String locale;
    private String dateFormat;
    private Map<String, String> columns;
    private Map<String, Integer> columnsOrder;

    public Settings(SheetData sheet, List<SheetData> sheets){
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

    @JsonProperty("dateParser")
    public DateFormat getDateParser(){
        if (this.locale == null || this.dateFormat == null){
            return new SimpleDateFormat("dd MMMM", Locale.US);
        }

        Locale locale = new Locale(this.locale.toLowerCase(), this.locale.toUpperCase());
        return new SimpleDateFormat(this.dateFormat, locale);
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

    public String toJson() throws JsonProcessingException {
        return JsonMaker.serialize(this);
    }
}
