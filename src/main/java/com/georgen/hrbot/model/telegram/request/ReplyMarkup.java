package com.georgen.hrbot.model.telegram.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.georgen.hrbot.model.chat.ReplyScheme;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReplyMarkup {
    private List<List<InlineButton>> inlineKeyboard;

    public ReplyMarkup() {}

    public ReplyMarkup(List<InlineButton> inlineButtons) {
        this.inlineKeyboard = inlineButtons
                .stream()
                .map(button -> new ArrayList<InlineButton>(){{ add(button); }})
                .collect(Collectors.toList());
    }
    public ReplyMarkup(ReplyScheme replyScheme){

    }

    @JsonProperty("inline_keyboard")
    public List<List<InlineButton>> getInlineKeyboard() {
        return inlineKeyboard;
    }

    public void setInlineKeyboard(List<List<InlineButton>> inlineKeyboard) {
        this.inlineKeyboard = inlineKeyboard;
    }
}
