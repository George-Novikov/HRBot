package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.model.chat.ChatReplies;
import com.fatemorgan.hrbot.model.constants.TelegramApiParam;
import com.fatemorgan.hrbot.model.serializers.TelegramRequestSerializer;
import com.fatemorgan.hrbot.model.serializers.TelegramResponseSerializer;
import com.fatemorgan.hrbot.model.settings.SettingsGlobalContainer;
import com.fatemorgan.hrbot.model.settings.TelegramSettings;
import com.fatemorgan.hrbot.model.telegram.request.TelegramMessageRequest;
import com.fatemorgan.hrbot.model.telegram.response.TelegramInfoResponse;
import com.fatemorgan.hrbot.model.telegram.response.messages.TelegramMessage;
import com.fatemorgan.hrbot.model.telegram.response.messages.TelegramMessageResponse;
import com.fatemorgan.hrbot.network.HttpConnector;
import com.fatemorgan.hrbot.network.UrlParamBuilder;
import com.fatemorgan.hrbot.storage.MessageStorage;
import com.fatemorgan.hrbot.tools.SafeReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TelegramApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramApi.class);
    @Value("${telegram.chat-id}")
    private Long defaultChatID;

    private MessageStorage messageStorage;

    public TelegramApi(MessageStorage messageStorage) {
        this.messageStorage = messageStorage;
    }

    public String getBotInfo() throws Exception {
        try (HttpConnector connector = new HttpConnector(buildUrl())){
            return connector.get("/getMe", "");
        }
    }

    public TelegramInfoResponse getBotInfoResponse(){
        TelegramInfoResponse infoResponse = null;

        try {
            infoResponse = TelegramResponseSerializer.deserializeInfo(getBotInfo());
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }

        return infoResponse;
    }

    public String getAvailableChats(){
        //TODO
        return null;
    }

    public String sendMessage(String message) throws Exception {
        try (HttpConnector connector = new HttpConnector(buildUrl())){
            return connector.get("/sendMessage", buildMessageParams(message, this.defaultChatID));
        }
    }

    public String sendMessage(String message, Long chatID) throws Exception {
        try (HttpConnector connector = new HttpConnector(buildUrl())){
            return connector.get("/sendMessage", buildMessageParams(message, chatID != null ? chatID : this.defaultChatID));
        }
    }

    public String sendSticker(String fileID, Long chatID) throws Exception{
        try (HttpConnector connector = new HttpConnector(buildUrl())){
            return connector.get("/sendSticker", buildStickerParams(fileID, chatID));
        }
    }

    public String getStickerSet(String stickerSetCode) throws Exception {
        try (HttpConnector connector = new HttpConnector(buildUrl())){
            return connector.get("/getStickerSet", buildStickerSetParams(stickerSetCode));
        }
    }

    public String reply(String replyText, TelegramMessage message) throws Exception {
        return reply(replyText, message.getMessageID(), message.getChatID());
    }

    public String reply(String text, Long repliedMessageID, Long chatID) throws Exception {
        if (!SafeReader.isValid(text)) return null;

        try (HttpConnector connector = new HttpConnector(buildUrl())){
            TelegramMessageRequest request = new TelegramMessageRequest(text, repliedMessageID);
            return connector.post(
                    "/sendMessage",
                    buildMessageParams(text, chatID != null ? chatID : defaultChatID),
                    TelegramRequestSerializer.serialize(request)
            );
        }
    }

    public String reply(TelegramMessageRequest request, Long chatID) throws Exception {
        if (request == null || request.isEmpty()) return null;

        try (HttpConnector connector = new HttpConnector(buildUrl())){
            return connector.post(
                    "/sendMessage",
                    buildMessageParams(chatID != null ? chatID : defaultChatID),
                    TelegramRequestSerializer.serialize(request)
            );
        }
    }

    public TelegramMessageResponse getUpdates() {
        TelegramMessageResponse response = null;

        try (HttpConnector connector = new HttpConnector(buildUrl())){
            String jsonResponse = connector.get("/getUpdates");
            if (jsonResponse != null) response = TelegramResponseSerializer.deserializeMessages(jsonResponse);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }

        if (response != null) response.cleanUpResiduals();

        return response;
    }

    public List<TelegramMessage> getUnansweredMessages(ChatReplies chatReplies) throws Exception {
        TelegramMessageResponse response = getUpdates();
        if (response == null || response.isEmpty()) return null;

        List<TelegramMessage> messages = getMessages(response);
        List<TelegramMessage> unansweredMessages = filterUnanswered(messages);

        return unansweredMessages;
    }

    public String processUnansweredMessages(List<TelegramMessage> unanswered, ChatReplies chatReplies) throws Exception {
        Set<Long> repliedMessageIDs = replyAll(unanswered, chatReplies);
        messageStorage.saveReplies(repliedMessageIDs);

        return repliedMessageIDs.toString();
    }

    public String replyUnanswered(ChatReplies chatReplies) throws Exception {
        List<TelegramMessage> unanswered = getUnansweredMessages(chatReplies);

        Set<Long> repliedMessageIDs = replyAll(unanswered, chatReplies);
        messageStorage.saveReplies(repliedMessageIDs);

        return repliedMessageIDs.toString();
    }

    private Set<Long> replyAll(List<TelegramMessage> messages, ChatReplies chatReplies) throws Exception {
        if (messages == null || messages.isEmpty()) return new HashSet<>();

        Set<Long> repliedMessageIDs = new HashSet<>();
        for (TelegramMessage message : messages){
            if (chatReplies.isMenuRequested(message.getText())){
                sendMenu(message, repliedMessageIDs, chatReplies);
                continue;
            }

            String reply = chatReplies.getReply(message.getText());
            if (reply == null) continue;

            String jsonResponse = reply(reply, message);
            if (SafeReader.isValid(jsonResponse)) repliedMessageIDs.add(message.getMessageID());
        }
        return repliedMessageIDs;
    }

    private void sendMenu(TelegramMessage message, Set<Long> repliedMessageIDs, ChatReplies chatReplies) throws Exception {
        TelegramMessageRequest messageRequest = chatReplies.getMenuReply(message);
        String response = reply(messageRequest, message.getChatID());
        if (response != null && !response.isEmpty()) repliedMessageIDs.add(message.getMessageID());
    }

    private String buildMessageParams(Long chatID){
        return new UrlParamBuilder()
                .add(TelegramApiParam.CHAT_ID, chatID)
                .build();
    }

    private String buildMessageParams(String message, Long chatID) throws UnsupportedEncodingException {
        return new UrlParamBuilder()
                .add(TelegramApiParam.CHAT_ID, chatID)
                .add(TelegramApiParam.TEXT, URLEncoder.encode(message, "UTF-8"))
                .build();
    }

    private String buildStickerParams(String fileID, Long chatID) {
        return new UrlParamBuilder()
                .add(TelegramApiParam.CHAT_ID, chatID)
                .add(TelegramApiParam.STICKER, fileID)
                .build();
    }

    private String buildStickerSetParams(String stickerSetCode){
        return new UrlParamBuilder()
                .add(TelegramApiParam.NAME, stickerSetCode)
                .build();
    }

    private List<TelegramMessage> getMessages(TelegramMessageResponse response){
        return response.getResult().stream().map(res -> res.getMessage()).collect(Collectors.toList());
    }

    private List<TelegramMessage> getCitationMessages(List<TelegramMessage> messages){
        TelegramInfoResponse infoResponse = getBotInfoResponse();
        if (infoResponse == null || infoResponse.isEmpty()) return new ArrayList<>();

        String botNickName = String.format("@%s", infoResponse.getResult().getUserName());

        return messages
                .stream()
                .filter(message -> message.isCitation(botNickName))
                .collect(Collectors.toList());
    }

    private List<TelegramMessage> filterUnanswered(List<TelegramMessage> messages){
        return messages.stream().filter(message -> !isAnswered(message)).collect(Collectors.toList());
    }

    private String buildUrl(){
        TelegramSettings telegramSettings = SettingsGlobalContainer.getInstance().getTelegramSettings();
        //TODO: validate settings
        return String.format(
                "%s%s",
                telegramSettings.getApiUrl(),
                telegramSettings.getBotToken()
        );
    }

    private boolean isAnswered(TelegramMessage message){
        return messageStorage.isAnswered(message.getMessageID());
    }
}
