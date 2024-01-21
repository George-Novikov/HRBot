package com.fatemorgan.hrbot.model.constants;

public enum ChatMessage implements Descriptive {
    CHAT_REPLIES_LOADING_ERROR("Failed to load char replies from remote spreadsheet source."),
    EMPTY_CHAT_REPLIES("Chat replies scheme is empty.");

    private String description;

    ChatMessage(String description) {
        this.description = description;
    }

    @Override
    public String describe() {
        return this.description;
    }
}
