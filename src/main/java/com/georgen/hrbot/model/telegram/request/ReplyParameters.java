package com.georgen.hrbot.model.telegram.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReplyParameters {
    private Long messageID;

    public ReplyParameters() {}

    public ReplyParameters(Long messageID) {
        this.messageID = messageID;
    }

    @JsonProperty("message_id")
    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }
}
