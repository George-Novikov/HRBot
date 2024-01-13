package com.fatemorgan.hrbot.model.google;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.serializers.JsonMaker;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SheetData {
    private Integer sheetID;
    private String title;
    private String range;
    private List<List<String>> rows;

    public SheetData(Sheet sheet, String range){
        SheetProperties props = sheet.getProperties();
        if (props == null) return;

        this.sheetID = props.getSheetId();
        this.title = props.getTitle();
        this.range = String.format("%s!%s", this.title, range);
    }

    public Integer getSheetID() {
        return sheetID;
    }

    public void setSheetID(Integer sheetID) {
        this.sheetID = sheetID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<Object>> rows) {
        List<List<String>> stringRows = new ArrayList<>();
        for (List<Object> row : rows){
            List<String> stringRow = row
                    .stream()
                    .map(cell -> Objects.toString(cell, ""))
                    .collect(Collectors.toList());
            stringRows.add(stringRow);
        }
        this.rows = stringRows;
    }

    public String toJson() throws JsonProcessingException {
        return JsonMaker.serialize(this);
    }

    public boolean isEmpty(){
        return this.rows == null;
    }

    public boolean isProcessed(Action action){
        return action.toString().equals(this.title) || action.equals(Action.ALL);
    }
}
