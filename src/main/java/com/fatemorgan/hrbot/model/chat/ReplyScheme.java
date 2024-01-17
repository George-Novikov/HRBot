package com.fatemorgan.hrbot.model.chat;

import java.util.List;
import java.util.Objects;

public class ReplyScheme {
    private String request;
    private String reply;

    public ReplyScheme(String request, String reply) {
        this.request = request;
        this.reply = reply;
    }

    public String getRequest() {
        return request;
    }

    public String getReply() {
        return reply;
    }

    public boolean isRequested(String request){
        return this.request.trim().equalsIgnoreCase(request.trim());
    }
}
