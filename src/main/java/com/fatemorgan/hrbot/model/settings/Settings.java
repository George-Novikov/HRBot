package com.fatemorgan.hrbot.model.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fatemorgan.hrbot.model.constants.SettingsAttribute;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.serializers.JsonMaker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {
    private String locale;
    private String dateFormat;
    private Map<String, String> columns;

    public Settings(SheetData data){
        if (data.getRows() == null) return;
        this.columns = new HashMap();
        fill(data.getRows());
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

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, String> columns) {
        this.columns = columns;
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

    public String toJson() throws JsonProcessingException {
        return JsonMaker.serialize(this);
    }
}
