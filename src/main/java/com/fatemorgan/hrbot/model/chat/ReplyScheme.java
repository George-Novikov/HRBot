package com.fatemorgan.hrbot.model.chat;

import java.util.List;
import java.util.Objects;

public class ReplyScheme {
    private String request;
    private String reply;

    public ReplyScheme(List<Object> row){
        if (row == null || row.size() < 2) return;
        this.request = Objects.toString(row.get(0), "");
        this.reply = Objects.toString(row.get(1), "");
    }

    public String getRequest() {
        return request;
    }

    public String getReply() {
        return reply;
    }
}
