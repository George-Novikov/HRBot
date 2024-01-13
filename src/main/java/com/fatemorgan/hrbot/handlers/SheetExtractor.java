package com.fatemorgan.hrbot.handlers;

import com.fatemorgan.hrbot.model.google.SheetData;
import com.google.api.services.sheets.v4.model.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SheetExtractor {
    @Value("${google.sheets.settings}")
    private String settingsSheetTitle;
    @Value("${google.sheets.birthdays}")
    private String birthdaysSheetTitle;
    @Value("${google.sheets.events}")
    private String eventsSheetTitle;
    @Value("${google.sheets.chat}")
    private String chatSheetTitle;

    public SheetData getSettingsSheet(List<SheetData> sheets){
        return getSheet(sheets, settingsSheetTitle);
    }

    public SheetData getBirthdaysSheet(List<SheetData> sheets){
        return getSheet(sheets, birthdaysSheetTitle);
    }

    public SheetData getEventsSheet(List<SheetData> sheets){
        return getSheet(sheets, eventsSheetTitle);
    }

    public SheetData getChatSheet(List<SheetData> sheets){
        return getSheet(sheets, chatSheetTitle);
    }

    private SheetData getSheet(List<SheetData> sheets, String sheetTitle){
        return sheets
                .stream()
                .filter(sheet -> sheet.getTitle().equals(sheetTitle))
                .findFirst()
                .orElse(null);
    }
}
