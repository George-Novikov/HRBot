package com.fatemorgan.hrbot.model.constants;

public enum BirthdaysMessage implements Descriptive {
    BIRTHDAYS_LOADING_ERROR("Failed to load birthdays from remote spreadsheet source.");

    private String description;

    BirthdaysMessage(String description) {
        this.description = description;
    }

    @Override
    public String describe() {
        return this.description;
    }
}
