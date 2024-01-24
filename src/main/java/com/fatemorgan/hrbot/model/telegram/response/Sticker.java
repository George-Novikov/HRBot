package com.fatemorgan.hrbot.model.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sticker {
    private String emoji;
    private String setName;

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    @JsonProperty("set_name")
    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public boolean isEmpty(){
        return this.emoji == null || this.emoji.isEmpty();
    }
}
