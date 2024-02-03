package com.fatemorgan.hrbot.model.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fatemorgan.hrbot.model.constants.TelegramDefaults;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramSettings {
    private List<Long> chatIDs;
    private String stickerSetName;
    private String botToken;
    private String apiUrl;
    private int birthdaysUpdateRate;
    private int eventsUpdateRate;
    private int chatUpdateRate;

    public List<Long> getChatIDs() {
        if (chatIDs == null) return new ArrayList<>();
        return chatIDs;
    }

    public void setChatIDs(List<Long> chatIDs) {
        this.chatIDs = chatIDs;
    }

    public String getStickerSetName() {
        return stickerSetName;
    }

    public void setStickerSetName(String stickerSetName) {
        this.stickerSetName = stickerSetName;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getApiUrl() {
        if (apiUrl == null) return TelegramDefaults.DEFAULT_API_URL;
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public int getBirthdaysUpdateRate() {
        if (birthdaysUpdateRate == 0) return TelegramDefaults.DEFAULT_BIRTHDAYS_UPDATE_RATE;
        return birthdaysUpdateRate;
    }

    public void setBirthdaysUpdateRate(int birthdaysUpdateRate) {
        this.birthdaysUpdateRate = birthdaysUpdateRate;
    }

    public int getEventsUpdateRate() {
        if (eventsUpdateRate == 0) return TelegramDefaults.DEFAULT_EVENTS_UPDATE_RATE;
        return eventsUpdateRate;
    }

    public void setEventsUpdateRate(int eventsUpdateRate) {
        this.eventsUpdateRate = eventsUpdateRate;
    }

    public int getChatUpdateRate() {
        if (chatUpdateRate == 0) return TelegramDefaults.DEFAULT_CHAT_UPDATE_RATE;
        return chatUpdateRate;
    }

    public void setChatUpdateRate(int chatUpdateRate) {
        this.chatUpdateRate = chatUpdateRate;
    }
}
