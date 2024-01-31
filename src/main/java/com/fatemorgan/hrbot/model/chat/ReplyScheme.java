package com.fatemorgan.hrbot.model.chat;

import com.fatemorgan.hrbot.model.constants.Placeholder;
import com.fatemorgan.hrbot.model.serializers.InlineButtonSerializer;
import com.fatemorgan.hrbot.model.telegram.request.InlineButton;
import com.fatemorgan.hrbot.model.telegram.request.TelegramMessageRequest;
import com.fatemorgan.hrbot.tools.SafeReader;

import java.util.List;

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

    public TelegramMessageRequest getMenuReply(){
        return getMenuReply(null);
    }

    public TelegramMessageRequest getMenuReply(Long messageID){
        if (isEmpty()) return null;

        String[] splitReply = this.reply.split(Placeholder.MENU);

        if (splitReply.length < 2){
            return new TelegramMessageRequest(
                    this.reply.replace(Placeholder.MENU, ""),
                    messageID
            );
        }

        String text = splitReply[0];
        List<InlineButton> buttons = InlineButtonSerializer.deserialize(splitReply[1]);

        if (!SafeReader.isValid(text)) text = "_";
        return new TelegramMessageRequest(text, buttons, messageID);
    }

    public boolean hasRequest(){
        return request != null || !request.isEmpty();
    }

    public boolean hasReply(){
        return reply != null || !reply.isEmpty();
    }

    public boolean isEmpty(){
        return this.request == null || this.request.isEmpty() || this.reply == null || this.request.isEmpty();
    }

    public boolean isRequested(String request){
        if (!hasRequest()) return false;
        if (hasStickerRequest()) this.request = this.request.replace(Placeholder.STICKER, "");
        return this.request.trim().equalsIgnoreCase(request.trim());
    }

    public boolean hasStickerRequest(){
        if (!hasRequest()) return false;
        return this.request.startsWith(Placeholder.STICKER);
    }

    public boolean hasStickerReply(){
        if (!hasReply()) return false;
        return this.reply.startsWith(Placeholder.STICKER);
    }

    public boolean hasMenuReply(){
        if (!hasReply()) return false;
        return this.reply.contains(Placeholder.MENU);
    }
}
