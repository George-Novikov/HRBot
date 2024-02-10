package com.georgen.hrbot.model.constants;

public enum SystemMessage implements Descriptive {
    CHECK_EMPTY_SETTING("WARNING! Some of these settings are not set: chatIDs, botToken, spreadsheetID, userID."),
    BOT_IS_NOT_READY("Bot is running in standby mode because some settings are not initialized."),
    SETTINGS_CONTAINER_IS_INIT("The global settings container is initialized"),
    SETTINGS_HAVE_LOADED("Settings have loaded."),
    SETTINGS_LOADING_ERROR("Failed to load settings."),
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
