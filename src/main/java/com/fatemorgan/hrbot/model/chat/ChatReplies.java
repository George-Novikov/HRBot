package com.fatemorgan.hrbot.model.chat;

import com.fatemorgan.hrbot.model.constants.SettingsAttribute;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.tools.SafeReader;

import java.util.ArrayList;
import java.util.List;

public class ChatReplies {
    private List<ReplyScheme> replies;
    public ChatReplies(SheetData sheet, Settings settings){
        this.replies = new ArrayList<>();
        fillReplies(sheet, settings);
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
        request = request.replace(String.format("%s ", citation), "");
        return getReply(request);
    }

    private void fillReplies(SheetData sheet, Settings settings){
        if (sheet.isEmpty() || settings.isEmpty()) return;

        Integer requestIndex = settings.getColumnIndex(SettingsAttribute.REQUEST);
        Integer replyIndex = settings.getColumnIndex(SettingsAttribute.REPLY);

        //TODO: null check

        for (List<String> row : sheet.getRows()){
            if (row.isEmpty()) continue;

            String request = getSafeValue(row, requestIndex);
            String reply = getSafeValue(row, replyIndex);

            if (settings.isHeader(request) || settings.isHeader(reply)) continue;

            replies.add(new ReplyScheme(request, reply));
        }
    }

    private String getSafeValue(List<String> row, int index){
        return SafeReader.getValue(row, index);
    }

    public boolean isEmpty(){
        return this.replies == null || this.replies.isEmpty();
    }
}
