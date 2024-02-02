package com.fatemorgan.hrbot.tools;

import com.fatemorgan.hrbot.model.birthdays.BirthdaysSchedule;
import com.fatemorgan.hrbot.model.chat.ChatReplies;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.events.EventsSchedule;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.settings.DataSettings;
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

    public DataSettings getSettings(List<SheetData> sheets){
        SheetData settingsSheet = getSheet(sheets, settingsSheetTitle);
        return isValid(settingsSheet) ? new DataSettings(settingsSheet, sheets) : null;
    }

    public BirthdaysSchedule getBirthdays(List<SheetData> sheets, DataSettings dataSettings) throws DateParserException {
        SheetData birthdaysSheet = getSheet(sheets, birthdaysSheetTitle);
        return isValid(birthdaysSheet) ? new BirthdaysSchedule(birthdaysSheet, dataSettings) : null;
    }

    public EventsSchedule getEvents(List<SheetData> sheets, DataSettings dataSettings){
        SheetData eventsSheet = getSheet(sheets, eventsSheetTitle);
        return isValid(eventsSheet) ? new EventsSchedule(eventsSheet, dataSettings) : null;
    }

    public ChatReplies getChatReplies(List<SheetData> sheets, DataSettings dataSettings){
        SheetData chatSheet = getSheet(sheets, chatSheetTitle);
        return isValid(chatSheet) ? new ChatReplies(chatSheet, dataSettings) : null;
    }

    public boolean isExtracted(SheetData sheet, Action action){
        switch (action){
            case SETTINGS_UPDATE:
                return isSettingsExtraction(sheet);
            case BIRTHDAYS_UPDATE:
                return isBirthdaysExtraction(sheet) || isSettingsExtraction(sheet);
            case EVENTS_UPDATE:
                return isEventsExtraction(sheet) || isSettingsExtraction(sheet);
            case CHAT_UPDATE:
                return isChatExtraction(sheet) || isSettingsExtraction(sheet);
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

    private boolean isSettingsExtraction(SheetData sheet){
        return settingsSheetTitle.equals(sheet.getTitle());
    }

    private boolean isBirthdaysExtraction(SheetData sheet){
        return birthdaysSheetTitle.equals(sheet.getTitle());
    }

    private boolean isEventsExtraction(SheetData sheet){
        return eventsSheetTitle.equals(sheet.getTitle());
    }

    private boolean isChatExtraction(SheetData sheet){
        return chatSheetTitle.equals(sheet.getTitle());
    }

    private boolean isValid(SheetData sheet){
        return sheet != null && !sheet.isEmpty();
    }
}
