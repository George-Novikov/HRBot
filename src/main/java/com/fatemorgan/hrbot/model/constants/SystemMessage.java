package com.fatemorgan.hrbot.model.constants;

public enum SystemMessage implements Descriptive {
    SETTINGS_LOADING_ERROR("Failed to load settings from remote spreadsheet source."),
    SETTINGS_SAVE_SUCCESS("Settings saved successfully."),
    TEST_IS_OK("Successful test request. Please check the service logs for more information.");

    private String description;

    SystemMessage(String description) {
        this.description = description;
    }

    @Override
    public String describe() {
        return this.description;
    }
}
