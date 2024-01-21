package com.fatemorgan.hrbot.model.telegram.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fatemorgan.hrbot.model.telegram.response.entities.Chat;
import com.fatemorgan.hrbot.model.telegram.response.entities.Sender;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramMessage {
    private Long messageID;
    private Sender from;
    private Chat chat;
    private Long date;
    private String text;

    @JsonProperty("message_id")
    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }

    public Sender getFrom() {
        return from;
    }

    public void setFrom(Sender from) {
        this.from = from;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getChatID(){
        if (this.chat == null) return null;
        return this.chat.getId();
    }

    public boolean isEmpty(){
        return this.text == null || this.text.isEmpty();
    }

    public boolean isCitation(String nickname){
        if (isEmpty()) return false;
        return this.text.startsWith(nickname);
    }
}
