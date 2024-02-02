package com.fatemorgan.hrbot.model.settings;

import java.util.List;

public class TelegramSettings {
    private List<Long> chatIDs;
    private String stickerSetName;
    private String botToken;
    private String apiURL;
    private Integer birthdaysUpdateRate;
    private Integer eventsUpdateRate;
    private Integer chatUpdateRate;

    public List<Long> getChatIDs() {
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

    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public Integer getBirthdaysUpdateRate() {
        return birthdaysUpdateRate;
    }

    public void setBirthdaysUpdateRate(Integer birthdaysUpdateRate) {
        this.birthdaysUpdateRate = birthdaysUpdateRate;
    }

    public Integer getEventsUpdateRate() {
        return eventsUpdateRate;
    }

    public void setEventsUpdateRate(Integer eventsUpdateRate) {
        this.eventsUpdateRate = eventsUpdateRate;
    }

    public Integer getChatUpdateRate() {
        return chatUpdateRate;
    }

    public void setChatUpdateRate(Integer chatUpdateRate) {
        this.chatUpdateRate = chatUpdateRate;
    }
}
