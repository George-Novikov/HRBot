package com.fatemorgan.hrbot.model;

import com.fatemorgan.hrbot.model.settings.Settings;

import java.util.List;
import java.util.Map;

public class FieldsOrder {
    private Map<String, Integer> orderMap;

    public FieldsOrder(Settings settings, List<List<String>> rows){
        rows
                .stream()
                .forEach(row -> {

                });
    }
}
