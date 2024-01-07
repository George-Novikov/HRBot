package com.fatemorgan.hrbot.model.telegram.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TelegramRequest {
    private String text;
    private ReplyMarkup replyMarkup;
    private ReplyParameters replyParameters;

    public TelegramRequest() {}

    public TelegramRequest(String text) {
        this.text = text;
    }

    public TelegramRequest(String text, Long messageID) {
        this.text = text;
        this.replyParameters = new ReplyParameters(messageID);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("reply_markup")
    public ReplyMarkup getReplyMarkup() {
        return replyMarkup;
    }

    public void setReplyMarkup(ReplyMarkup replyMarkup) {
        this.replyMarkup = replyMarkup;
    }

    @JsonProperty("reply_parameters")
    public ReplyParameters getReplyParameters() {
        return replyParameters;
    }

    public void setReplyParameters(ReplyParameters replyParameters) {
        this.replyParameters = replyParameters;
    }
}
