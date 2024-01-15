package com.fatemorgan.hrbot.model.constants;

public enum EventsMessage implements Descriptive{
    EVENTS_LOADING_ERROR("Failed to load events from remote spreadsheet source.");

    private String description;

    EventsMessage(String description) {
        this.description = description;
    }

    @Override
    public String describe() {
        return description;
    }
}
