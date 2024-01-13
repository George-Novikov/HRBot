package com.fatemorgan.hrbot.model.constants;

public enum Action {
    ALL,
    SETTINGS_UPDATE,
    BIRTHDAYS_UPDATE,
    EVENTS_UPDATE,
    CHAT_UPDATE;

    public String toString(){
        return this.name();
    }
}
