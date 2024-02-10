package com.georgen.hrbot.model.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.georgen.hrbot.model.constants.GoogleDefaults;
import com.georgen.hrbot.model.google.SheetData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleSettings {
    private String spreadsheetID;
    private String userID;
    private String tokenPath;
    private String credentialsPath;
    private String appName;
    private int receiverPort;
    private String birthdaysSheetName;
    private String eventsSheetName;
    private String chatRepliesSheetName;
    private String rangeToRead;

    public String getSpreadsheetID() {
        return spreadsheetID;
    }

    public void setSpreadsheetID(String spreadsheetID) {
        this.spreadsheetID = spreadsheetID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTokenPath() {
        if (tokenPath == null) return GoogleDefaults.DEFAULT_TOKEN_PATH;
        return tokenPath;
    }

    public void setTokenPath(String tokenPath) {
        this.tokenPath = tokenPath;
    }

    public String getCredentialsPath() {
        if (credentialsPath == null) return GoogleDefaults.DEFAULT_CREDENTIALS_PATH;
        return credentialsPath;
    }

    public void setCredentialsPath(String credentialsPath) {
        this.credentialsPath = credentialsPath;
    }

    public String getAppName() {
        if (appName == null) return GoogleDefaults.DEFAULT_APP_NAME;
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getReceiverPort() {
        if (receiverPort == 0) return GoogleDefaults.DEFAULT_RECEIVER_PORT;
        return receiverPort;
    }

    public void setReceiverPort(int receiverPort) {
        this.receiverPort = receiverPort;
    }

    public String getBirthdaysSheetName() {
        if (birthdaysSheetName == null) return GoogleDefaults.DEFAULT_BIRTHDAYS_SHEET_NAME;
        return birthdaysSheetName;
    }

    public void setBirthdaysSheetName(String birthdaysSheetName) {
        this.birthdaysSheetName = birthdaysSheetName;
    }

    public String getEventsSheetName() {
        if (eventsSheetName == null) return GoogleDefaults.DEFAULT_EVENTS_SHEET_NAME;
        return eventsSheetName;
    }

    public void setEventsSheetName(String eventsSheetName) {
        this.eventsSheetName = eventsSheetName;
    }

    public String getChatRepliesSheetName() {
        if (chatRepliesSheetName == null) return GoogleDefaults.DEFAULT_CHAT_REPLIES_SHEET_NAME;
        return chatRepliesSheetName;
    }

    public void setChatRepliesSheetName(String chatRepliesSheetName) {
        this.chatRepliesSheetName = chatRepliesSheetName;
    }

    public String getRangeToRead() {
        if (rangeToRead == null) return GoogleDefaults.DEFAULT_RANGE_TO_READ;
        return rangeToRead;
    }

    public void setRangeToRead(String rangeToRead) {
        this.rangeToRead = rangeToRead;
    }

    public boolean isBirthdaysSheet(SheetData sheet){
        return this.getBirthdaysSheetName().equals(sheet.getTitle());
    }

    public boolean isEventsSheet(SheetData sheet){
        return this.getEventsSheetName().equals(sheet.getTitle());
    }

    public boolean isChatRepliesSheet(SheetData sheet){
        return this.getChatRepliesSheetName().equals(sheet.getTitle());
    }

    public boolean isEmpty(){
        return this.getUserID() == null || this.getSpreadsheetID() == null;
    }
}
