package com.fatemorgan.hrbot.model.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TelegramUpdateResult {
    private Long updateID;
    private TelegramMessage message;

    @JsonProperty("update_id")
    public Long getUpdateID() {
        return updateID;
    }

    public void setUpdateID(Long updateID) {
        this.updateID = updateID;
    }

    public TelegramMessage getMessage() {
        return message;
    }

    public void setMessage(TelegramMessage message) {
        this.message = message;
    }

    public boolean isEmpty(){
        return this.message == null || this.message.isEmpty();
    }
    public boolean isCitation(String nickname){
        if (isEmpty()) return false;
        return this.message.isCitation(nickname);
    }
}
