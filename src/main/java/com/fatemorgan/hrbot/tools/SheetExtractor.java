package com.fatemorgan.hrbot.tools;

import com.fatemorgan.hrbot.model.birthdays.BirthdaysSchedule;
import com.fatemorgan.hrbot.model.chat.ChatReplies;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.events.EventsSchedule;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.settings.Settings;
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

    public Settings getSettings(List<SheetData> sheets){
        SheetData settingsSheet = getSheet(sheets, settingsSheetTitle);
        return settingsSheet != null && !settingsSheet.isEmpty() ? new Settings(settingsSheet) : null;
    }

    public BirthdaysSchedule getBirthdays(List<SheetData> sheets){
        SheetData birthdaysSheet = getSheet(sheets, birthdaysSheetTitle);
        return birthdaysSheet != null && !birthdaysSheet.isEmpty() ? new BirthdaysSchedule(birthdaysSheet) : null;
    }

    public EventsSchedule getEvents(List<SheetData> sheets){
        SheetData eventsSheet = getSheet(sheets, eventsSheetTitle);
        return eventsSheet != null && !eventsSheet.isEmpty()? new EventsSchedule(eventsSheet) : null;
    }

    public ChatReplies getChatReplies(List<SheetData> sheets){
        SheetData chatSheet = getSheet(sheets, chatSheetTitle);
        return chatSheet != null && !chatSheet.isEmpty() ? new ChatReplies(chatSheet) : null;
    }

    public boolean isExtracted(SheetData sheet, Action action){
        switch (action){
            case SETTINGS_UPDATE:
                return settingsSheetTitle.equals(sheet.getTitle());
            case BIRTHDAYS_UPDATE:
                return birthdaysSheetTitle.equals(sheet.getTitle());
            case EVENTS_UPDATE:
                return eventsSheetTitle.equals(sheet.getTitle());
            case CHAT_UPDATE:
                return chatSheetTitle.equals(sheet.getTitle());
            case ALL:
                return true;
            default:
                return false;
        }
    }

    private SheetData getSheet(List<SheetData> sheets, String sheetTitle){
        return sheets
                .stream()
                .filter(sheet -> sheet.getTitle().equals(sheetTitle))
                .findFirst()
                .orElse(null);
    }
}
