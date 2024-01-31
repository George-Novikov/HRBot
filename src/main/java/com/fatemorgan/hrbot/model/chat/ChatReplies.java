package com.fatemorgan.hrbot.model.chat;

import com.fatemorgan.hrbot.model.constants.Placeholder;
import com.fatemorgan.hrbot.model.constants.SettingsAttribute;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.model.telegram.request.TelegramMessageRequest;
import com.fatemorgan.hrbot.model.telegram.response.messages.TelegramMessage;
import com.fatemorgan.hrbot.tools.SafeReader;

import java.util.ArrayList;
import java.util.List;

public class ChatReplies {
    private String nextBirthdayRequest;
    private List<ReplyScheme> replies;
    private List<ReplyScheme> stickerReplies;
    private List<ReplyScheme> menuReplies;
    public ChatReplies(SheetData sheet, Settings settings){
        this.nextBirthdayRequest = settings.getNextBirthdayRequest();
        this.replies = new ArrayList<>();
        this.stickerReplies = new ArrayList<>();
        this.menuReplies = new ArrayList<>();
        fillReplies(sheet, settings);
    }

    public String getNextBirthdayRequest() {
        return nextBirthdayRequest;
    }

    public void setNextBirthdayRequest(String nextBirthdayRequest) {
        this.nextBirthdayRequest = nextBirthdayRequest;
    }

    public List<ReplyScheme> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyScheme> replies) {
        this.replies = replies;
    }

    public String getReply(String request){
        return this.replies
                .stream()
                .filter(scheme -> scheme.isRequested(request))
                .map(scheme -> scheme.getReply())
                .findFirst()
                .orElse("");
    }

    public String getCitationReply(String request, String citation){
        request = request.replace(citation, "");
        return getReply(request);
    }

    private void fillReplies(SheetData sheet, Settings settings){
        if (sheet.isEmpty() || settings.isEmpty()) return;

        Integer requestIndex = settings.getColumnIndex(SettingsAttribute.REQUEST);
        Integer replyIndex = settings.getColumnIndex(SettingsAttribute.REPLY);

        if (requestIndex == null || replyIndex == null) return;

        for (List<String> row : sheet.getRows()){
            if (row.isEmpty()) continue;

            String request = getSafeValue(row, requestIndex);
            String reply = getSafeValue(row, replyIndex);

            if (settings.isHeader(request) || settings.isHeader(reply)) continue;

            if (isStickerReply(reply)){
                stickerReplies.add(new ReplyScheme(request, reply));
                continue;
            }

            if (isMenuReply(reply)){
                menuReplies.add(new ReplyScheme(request, reply));
                continue;
            }

            replies.add(new ReplyScheme(request, reply));
        }
    }

    public TelegramMessage extractBirthdayRequest(List<TelegramMessage> messages){
        if (this.nextBirthdayRequest == null || messages == null) return null;
        return messages
                .stream()
                .filter(message -> !message.isEmpty() && message.isRequested(nextBirthdayRequest))
                .findFirst()
                .orElse(null);
    }

    public TelegramMessageRequest getMenuReply(TelegramMessage message){
        if (message == null || message.isEmpty() || this.menuReplies == null) return null;

        ReplyScheme replyScheme = getMenuReplyScheme(message.getText());
        if (replyScheme == null || !replyScheme.hasMenuReply()) return null;

        return replyScheme.getMenuReply();
    }

    public boolean isMenuRequested(String request){
        if (request == null || this.menuReplies == null) return false;

        ReplyScheme replyScheme = getMenuReplyScheme(request);
        if (replyScheme == null) return false;

        return replyScheme.hasMenuReply();
    }

    private ReplyScheme getMenuReplyScheme(String request){
        if (request == null || this.menuReplies == null) return null;

        return this.menuReplies
                .stream()
                .filter(scheme -> scheme.isRequested(request))
                .findFirst()
                .orElse(null);
    }

    public boolean hasNextBirthdayRequest(List<TelegramMessage> messages){
        if (this.nextBirthdayRequest == null || messages == null) return false;
        return messages
                .stream()
                .anyMatch(message -> !message.isEmpty() && message.isRequested(nextBirthdayRequest));
    }

    private String getSafeValue(List<String> row, int index){
        return SafeReader.getValue(row, index);
    }

    public boolean isEmpty(){
        return this.replies == null || this.replies.isEmpty();
    }

    private boolean isStickerReply(String text){
        if (text == null) return false;
        return text.startsWith(Placeholder.STICKER);
    }

    private boolean isMenuReply(String text){
        if (text == null) return false;
        return text.contains(Placeholder.MENU);
    }
}
