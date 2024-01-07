package com.fatemorgan.hrbot.config;

import org.springframework.beans.factory.annotation.Value;

public class GoogleConfig {
    @Value("${google.api-key}")
    private String apiKey;
    @Value("${google.url}")
    private String url;
    @Value("${google.spreadsheet-id}")
    private String spreadsheetID;

    public String getApiKey() {
        return apiKey;
    }

    public String getUrl() {
        return url;
    }

    public String getSpreadsheetID() {
        return spreadsheetID;
    }
}
