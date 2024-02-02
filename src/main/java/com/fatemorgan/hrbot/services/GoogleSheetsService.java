package com.fatemorgan.hrbot.services;


import com.fatemorgan.hrbot.model.GlobalDataContainer;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.settings.DataSettings;
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
    @Value("${google.spreadsheet-id}")
    private String spreadsheetID;
    @Value("${google.sheets.range-to-read}")
    private String range;
    private Sheets sheetsService;
    private SheetExtractor extractor;
    private GlobalDataContainer container;

    public GoogleSheetsService(Sheets sheetsService,
                               SheetExtractor extractor,
                               GlobalDataContainer container) {
        this.sheetsService = sheetsService;
        this.extractor = extractor;
        this.container = container;
    }

    public void updateGlobalContainer(Action action) throws IOException, SettingsException, DateParserException {
        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetID).execute();

        List<SheetData> sheets = spreadsheet.getSheets()
                .stream()
                .map(sheet -> new SheetData(sheet, range))
                .collect(Collectors.toList());
        fillAllSheetsData(sheets, action);

        DataSettings dataSettings = extractor.getSettings(sheets);
        if (dataSettings == null) throw new SettingsException("Failed to load settings from Spreadsheets remote source");

        BirthdaysSchedule birthdays = extractor.getBirthdays(sheets, dataSettings);
        EventsSchedule events = extractor.getEvents(sheets, dataSettings);
        ChatReplies chatReplies = extractor.getChatReplies(sheets, dataSettings);

        container.load(dataSettings, birthdays, events, chatReplies);
    }

    private void fillAllSheetsData(List<SheetData> sheets, Action action) throws IOException {
        for (SheetData sheet : sheets){
            if (extractor.isExtracted(sheet, action)){
                getSheetData(sheet);
            }
        }
    }

    private void getSheetData(SheetData sheet) throws IOException {
        ValueRange valueRange = sheetsService
                .spreadsheets()
                .values()
                .get(spreadsheetID, sheet.getRange())
                .execute();

        if (valueRange == null || valueRange.getValues() == null) return;

        sheet.setRows(valueRange.getValues());
    }
}
