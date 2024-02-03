package com.fatemorgan.hrbot.tools;

import com.fatemorgan.hrbot.model.birthdays.BirthdaysSchedule;
import com.fatemorgan.hrbot.model.chat.ChatReplies;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.events.EventsSchedule;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.settings.DataSettings;
import com.fatemorgan.hrbot.model.settings.GoogleSettings;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.model.settings.SettingsGlobalContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SheetExtractor {

    public BirthdaysSchedule getBirthdays(List<SheetData> sheets) throws DateParserException {
        Settings settings = SettingsGlobalContainer.getInstance();
        GoogleSettings googleSettings = settings.getGoogleSettings();
        SheetData birthdaysSheet = getSheet(sheets, googleSettings.getBirthdaysSheetName());
        return isValid(birthdaysSheet) ? new BirthdaysSchedule(birthdaysSheet, settings.getDataSettings()) : null;
    }

    public EventsSchedule getEvents(List<SheetData> sheets){
        Settings settings = SettingsGlobalContainer.getInstance();
        GoogleSettings googleSettings = settings.getGoogleSettings();
        SheetData eventsSheet = getSheet(sheets, googleSettings.getEventsSheetName());
        return isValid(eventsSheet) ? new EventsSchedule(eventsSheet, settings.getDataSettings()) : null;
    }

    public ChatReplies getChatReplies(List<SheetData> sheets){
        Settings settings = SettingsGlobalContainer.getInstance();
        GoogleSettings googleSettings = settings.getGoogleSettings();
        SheetData chatSheet = getSheet(sheets, googleSettings.getChatRepliesSheetName());
        return isValid(chatSheet) ? new ChatReplies(chatSheet, settings.getDataSettings()) : null;
    }

    public boolean isExtracted(SheetData sheet, Action action){
        switch (action){
            case BIRTHDAYS_UPDATE:
                return isBirthdaysExtraction(sheet);
            case EVENTS_UPDATE:
                return isEventsExtraction(sheet);
            case CHAT_UPDATE:
                return isChatExtraction(sheet);
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

    private boolean isBirthdaysExtraction(SheetData sheet){
        return SettingsGlobalContainer.getInstance().getGoogleSettings().isBirthdaysSheet(sheet);
    }

    private boolean isEventsExtraction(SheetData sheet){
        return SettingsGlobalContainer.getInstance().getGoogleSettings().isEventsSheet(sheet);
    }

    private boolean isChatExtraction(SheetData sheet){
        return SettingsGlobalContainer.getInstance().getGoogleSettings().isChatRepliesSheet(sheet);
    }

    private boolean isValid(SheetData sheet){
        return sheet != null && !sheet.isEmpty();
    }
}
