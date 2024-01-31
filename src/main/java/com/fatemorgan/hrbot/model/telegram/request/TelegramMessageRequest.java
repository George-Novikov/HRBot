package com.fatemorgan.hrbot.model.telegram.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fatemorgan.hrbot.model.chat.ReplyScheme;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(value = {"isEmpty"}, ignoreUnknown = true)
public class TelegramMessageRequest {
    private String text;
    private ReplyMarkup replyMarkup;
    private ReplyParameters replyParameters;

    public TelegramMessageRequest() {}

    public TelegramMessageRequest(String text) {
        this.text = text;
    }

    public TelegramMessageRequest(String text, Long messageID) {
        this.text = text;
        if (messageID != null) this.replyParameters = new ReplyParameters(messageID);
    }

    public TelegramMessageRequest(String text, List<InlineButton> inlineButtons, Long messageID){
        this.text = text;
        this.replyMarkup = new ReplyMarkup(inlineButtons);
        if (messageID != null) this.replyParameters = new ReplyParameters(messageID);
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

    @JsonProperty("isEmpty")
    public boolean isEmpty(){
        return this.text == null && this.replyMarkup == null;
    }
}
