package com.fatemorgan.hrbot.model.settings;

public class Settings {
    private DataSettings dataSettings;
    private GoogleSettings googleSettings;
    private TelegramSettings telegramSettings;

    public DataSettings getDataSettings() {
        return dataSettings;
    }

    public void setDataSettings(DataSettings dataSettings) {
        this.dataSettings = dataSettings;
    }

    public GoogleSettings getGoogleSettings() {
        return googleSettings;
    }

    public void setGoogleSettings(GoogleSettings googleSettings) {
        this.googleSettings = googleSettings;
    }

    public TelegramSettings getTelegramSettings() {
        return telegramSettings;
    }

    public void setTelegramSettings(TelegramSettings telegramSettings) {
        this.telegramSettings = telegramSettings;
    }
}
