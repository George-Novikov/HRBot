package com.fatemorgan.hrbot.model.telegram.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ReplyMarkup {
    private List<List<InlineButton>> inlineKeyboard;

    public ReplyMarkup() {}

    public ReplyMarkup(List<List<InlineButton>> inlineKeyboard) {
        this.inlineKeyboard = inlineKeyboard;
    }

    public ReplyMarkup(ArrayList<InlineButton> inlineButtons) {
        this.inlineKeyboard = new ArrayList<>();
        this.inlineKeyboard.add(inlineButtons);
    }

    @JsonProperty("inline_keyboard")
    public List<List<InlineButton>> getInlineKeyboard() {
        return inlineKeyboard;
    }

    public void setInlineKeyboard(List<List<InlineButton>> inlineKeyboard) {
        this.inlineKeyboard = inlineKeyboard;
    }
}
