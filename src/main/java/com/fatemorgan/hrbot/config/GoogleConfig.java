package com.fatemorgan.hrbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleConfig {
    @Value("${google.api-key}")
    private String apiKey;
    @Value("${google.url}")
    private String url;
    @Value("${google.spreadsheet-id}")
    private String spreadsheetID;
    @Value("${google.user-id}")
    private String userID;

    public String getApiKey() {
        return apiKey;
    }

    public String getUrl() {
        return url;
    }

    public String getSpreadsheetID() {
        return spreadsheetID;
    }

    public String getUserID() {
        return userID;
    }
}
