package com.fatemorgan.hrbot.services;


import com.fatemorgan.hrbot.model.DataGlobalContainer;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.settings.DataSettings;
import com.fatemorgan.hrbot.model.settings.GoogleSettings;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.model.settings.SettingsGlobalContainer;
import com.fatemorgan.hrbot.tools.SheetExtractor;
import com.fatemorgan.hrbot.model.birthdays.BirthdaysSchedule;
import com.fatemorgan.hrbot.model.chat.ChatReplies;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.events.EventsSchedule;
import com.fatemorgan.hrbot.model.exceptions.SettingsException;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoogleSheetsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetsService.class);
    private Sheets sheetsService;
    private SheetExtractor extractor;
    private DataGlobalContainer container;

    public GoogleSheetsService(Sheets sheetsService,
                               SheetExtractor extractor,
                               DataGlobalContainer container) {
        this.sheetsService = sheetsService;
        this.extractor = extractor;
        this.container = container;
    }

    public void updateGlobalContainer(Action action) throws IOException, DateParserException {

        /* Any type of settings can't be null */
        Settings settings = SettingsGlobalContainer.getInstance();
        GoogleSettings googleSettings = settings.getGoogleSettings();
        DataSettings dataSettings = settings.getDataSettings();

        //TODO: validate settings

        String spreadsheetID = googleSettings.getSpreadsheetID();
        String range = googleSettings.getRangeToRead();
        if (spreadsheetID == null || range == null) return;

        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetID).execute();

        List<SheetData> sheets = spreadsheet.getSheets()
                .stream()
                .map(sheet -> new SheetData(sheet, range))
                .collect(Collectors.toList());
        fillAllSheetsData(sheets, action, spreadsheetID);

        dataSettings.fillColumnsOrder(sheets);

        BirthdaysSchedule birthdays = extractor.getBirthdays(sheets);
        EventsSchedule events = extractor.getEvents(sheets);
        ChatReplies chatReplies = extractor.getChatReplies(sheets);

        container.load(dataSettings, birthdays, events, chatReplies);
    }

    private void fillAllSheetsData(List<SheetData> sheets, Action action, String spreadsheetID) throws IOException {
        for (SheetData sheet : sheets){
            if (extractor.isExtracted(sheet, action)){
                getSheetData(sheet, spreadsheetID);
            }
        }
    }

    private void getSheetData(SheetData sheet, String spreadsheetID) throws IOException {
        ValueRange valueRange = sheetsService
                .spreadsheets()
                .values()
                .get(spreadsheetID, sheet.getRange())
                .execute();

        if (valueRange == null || valueRange.getValues() == null) return;

        sheet.setRows(valueRange.getValues());
    }
}
