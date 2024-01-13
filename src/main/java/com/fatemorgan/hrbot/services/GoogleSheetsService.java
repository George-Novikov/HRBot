package com.fatemorgan.hrbot.services;


import com.fatemorgan.hrbot.handlers.SheetExtractor;
import com.fatemorgan.hrbot.model.exceptions.SheetsException;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoogleSheetsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetsService.class);
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMMM", new Locale("ru", "RU"));
    @Value("${google.spreadsheet-id}")
    private String spreadsheetID;
    @Value("${google.sheets.range-to-read}")
    private String range;
    private Sheets sheetsService;
    private SheetExtractor extractor;

    public GoogleSheetsService(Sheets sheetsService, SheetExtractor extractor) {
        this.sheetsService = sheetsService;
        this.extractor = extractor;
    }

    public void getSheetsData() throws IOException, SheetsException {

        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetID).execute();
        List<SheetData> sheets = spreadsheet.getSheets()
                .stream()
                .map(sheet -> new SheetData(sheet, range))
                .collect(Collectors.toList());

        for (SheetData sheet : sheets){
            ValueRange valueRange = sheetsService
                    .spreadsheets()
                    .values()
                    .get(spreadsheetID, sheet.getRange())
                    .execute();

            if (valueRange == null || valueRange.getValues() == null) continue;

            sheet.setRows(valueRange.getValues());
        }


        SheetData settingsSheet = extractor.getSettingsSheet(sheets);
        SheetData birthdaysSheet = extractor.getBirthdaysSheet(sheets);
        SheetData eventsSheet = extractor.getEventsSheet(sheets);
        SheetData chatSheet = extractor.getChatSheet(sheets);

        if (settingsSheet != null){
            Settings settings = new Settings(settingsSheet);
            if (settings != null) LOGGER.info(settings.toJson());
        }
    }
}
