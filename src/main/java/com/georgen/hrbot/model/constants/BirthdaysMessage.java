package com.georgen.hrbot.model.constants;

public enum BirthdaysMessage implements Descriptive {
    BIRTHDAYS_LOADING_ERROR("Failed to load birthdays from remote spreadsheet source."),
    BIRTHDAYS_TASK_DOES_NOTHING("The Birthdays task is active but does nothing because the current time is not a business hour.");

    private String description;

    BirthdaysMessage(String description) {
        this.description = description;
    }

    @Override
    public String describe() {
        return this.description;
    }
}
