package com.fatemorgan.hrbot.model.settings;

public class GoogleSettings {
    private String spreadsheetID;
    private String userID;
    private String tokenPath;
    private String credentialsPath;
    private String appName;
    private String receiverPort;
    private String birthdaysSheetName;
    private String eventsSheetName;
    private String repliesSheetName;
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
        return tokenPath;
    }

    public void setTokenPath(String tokenPath) {
        this.tokenPath = tokenPath;
    }

    public String getCredentialsPath() {
        return credentialsPath;
    }

    public void setCredentialsPath(String credentialsPath) {
        this.credentialsPath = credentialsPath;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getReceiverPort() {
        return receiverPort;
    }

    public void setReceiverPort(String receiverPort) {
        this.receiverPort = receiverPort;
    }

    public String getBirthdaysSheetName() {
        return birthdaysSheetName;
    }

    public void setBirthdaysSheetName(String birthdaysSheetName) {
        this.birthdaysSheetName = birthdaysSheetName;
    }

    public String getEventsSheetName() {
        return eventsSheetName;
    }

    public void setEventsSheetName(String eventsSheetName) {
        this.eventsSheetName = eventsSheetName;
    }

    public String getRepliesSheetName() {
        return repliesSheetName;
    }

    public void setRepliesSheetName(String repliesSheetName) {
        this.repliesSheetName = repliesSheetName;
    }

    public String getRangeToRead() {
        return rangeToRead;
    }

    public void setRangeToRead(String rangeToRead) {
        this.rangeToRead = rangeToRead;
    }
}
