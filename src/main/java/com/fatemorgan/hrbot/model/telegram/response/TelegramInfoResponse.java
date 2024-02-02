package com.fatemorgan.hrbot.model.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = {"isEmpty"}, ignoreUnknown = true)
public class TelegramInfoResponse {
    private Boolean ok;
    private BotInfo result;

    public TelegramInfoResponse() {}
    public TelegramInfoResponse(Boolean ok, BotInfo result) {
        this.ok = ok;
        this.result = result;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public BotInfo getResult() {
        return result;
    }

    public void setResult(BotInfo result) {
        this.result = result;
    }

    @JsonProperty("isEmpty")
    public boolean isEmpty(){
        return this.ok == null || this.result == null || this.result.isEmpty();
    }
}
