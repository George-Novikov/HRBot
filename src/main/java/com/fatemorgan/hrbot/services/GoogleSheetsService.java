package com.fatemorgan.hrbot.services;


import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class GoogleSheetsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetsService.class);
    @Value("${google.spreadsheet-id}")
    private String spreadsheetID;
    private Sheets sheets;

    public GoogleSheetsService(Sheets sheets) {
        this.sheets = sheets;
    }

    public void printSheets() throws GeneralSecurityException, IOException {
        final String range = "A1:G10";

        ValueRange valueRange = sheets
                .spreadsheets()
                .values()
                .get(spreadsheetID, range)
                .execute();

        List<List<Object>> values = valueRange.getValues();

        if (values != null){
            for (List row : values){
                for (Object cell : row){
                    LOGGER.info("Cell: {}", cell);
                }
            }
        }
    }
}
