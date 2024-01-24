package com.fatemorgan.hrbot.model.chat;

import com.fatemorgan.hrbot.model.constants.Placeholder;

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
        if (isStickerScheme()) this.request = this.request.replace(Placeholder.STICKER, "");
        return this.request.trim().equalsIgnoreCase(request.trim());
    }

    public boolean isStickerScheme(){
        if (request == null || request.isEmpty()) return false;
        return this.request.startsWith(Placeholder.STICKER);
    }
}
