package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.model.chat.ChatReplies;
import com.fatemorgan.hrbot.model.constants.TelegramApiParam;
import com.fatemorgan.hrbot.model.serializers.JsonMaker;
import com.fatemorgan.hrbot.model.serializers.TelegramRequestSerializer;
import com.fatemorgan.hrbot.model.serializers.TelegramResponseSerializer;
import com.fatemorgan.hrbot.model.telegram.request.TelegramRequest;
import com.fatemorgan.hrbot.model.telegram.response.TelegramMessage;
import com.fatemorgan.hrbot.model.telegram.response.TelegramResponse;
import com.fatemorgan.hrbot.network.HttpConnector;
import com.fatemorgan.hrbot.network.UrlParamBuilder;
import com.fatemorgan.hrbot.storage.MessageStorage;
import com.fatemorgan.hrbot.tools.SafeReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TelegramApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramApi.class);
    private String botNickName;
    @Value("${telegram.url}")
    private String url;
    @Value("${telegram.bot-token}")
    private String botToken;
    @Value("${telegram.chat-id}")
    private Long defaultChatID;

    private MessageStorage messageStorage;

    public TelegramApi(MessageStorage messageStorage,
                       @Qualifier("botNickName") String botNickName) {
        this.messageStorage = messageStorage;
        this.botNickName = botNickName;
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

    public String reply(String replyText, TelegramMessage message) throws Exception {
        return reply(replyText, message.getMessageID(), message.getChatID());
    }

    public String reply(String text, Long repliedMessageID, Long chatID) throws Exception {
        if (!SafeReader.isValid(text)) return null;

        try (HttpConnector connector = new HttpConnector(buildUrl())){
            TelegramRequest request = new TelegramRequest(text, repliedMessageID);
            return connector.post(
                    "/sendMessage",
                    buildMessageParams(text, chatID != null ? chatID : defaultChatID),
                    TelegramRequestSerializer.serialize(request)
            );
        }
    }

    public TelegramResponse getUpdates() {
        TelegramResponse response = null;

        try (HttpConnector connector = new HttpConnector(buildUrl())){
            String jsonResponse = connector.get("/getUpdates");
            if (jsonResponse != null) response = TelegramResponseSerializer.deserialize(jsonResponse);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }

        if (response != null) response.cleanUpResiduals();

        return response;
    }

    public List<TelegramMessage> getUnansweredMessages(ChatReplies chatReplies) throws Exception {
        TelegramResponse response = getUpdates();
        if (response == null || response.isEmpty()) return null;

        List<TelegramMessage> messages = getMessages(response);
        List<TelegramMessage> citations = getCitationMessages(messages);
        List<TelegramMessage> stickers = getStickers(messages);
        if (!stickers.isEmpty()) processStickers(stickers, chatReplies);

        return filterUnanswered(citations);
    }

    public String processStickers(List<TelegramMessage> stickers, ChatReplies chatReplies) throws Exception {
        stickers = filterUnanswered(stickers);

        return processUnansweredMessages(stickers, chatReplies);
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
        Set<Long> repliedMessageIDs = new HashSet<>();
        for (TelegramMessage message : messages){
            String reply = message.isSticker() ? chatReplies.getCitationReply(message.getText(), botNickName) : chatReplies.getReply(message.getText());
            if (reply == null) continue;
            String jsonResponse = reply(reply, message);
            if (SafeReader.isValid(jsonResponse)) repliedMessageIDs.add(message.getMessageID());
        }
        return repliedMessageIDs;
    }

    private String buildMessageParams(String message, Long chatID) throws UnsupportedEncodingException {
        return new UrlParamBuilder()
                .add(TelegramApiParam.CHAT_ID, chatID)
                .add(TelegramApiParam.TEXT, URLEncoder.encode(message, "UTF-8"))
                .build();
    }

    private List<TelegramMessage> getMessages(TelegramResponse response){
        return response.getResult().stream().map(res -> res.getMessage()).collect(Collectors.toList());
    }

    private List<TelegramMessage> getCitationMessages(List<TelegramMessage> messages){
        return messages
                .stream()
                .filter(message -> message.isCitation(botNickName))
                .collect(Collectors.toList());
    }

    private List<TelegramMessage> getStickers(List<TelegramMessage> messages){
        return messages
                .stream()
                .filter(message -> message.isSticker())
                .collect(Collectors.toList());
    }

    private List<TelegramMessage> filterUnanswered(List<TelegramMessage> messages){
        return messages.stream().filter(message -> !isAnswered(message)).collect(Collectors.toList());
    }

    private String buildUrl(){
        return String.format("%s%s", url, botToken);
    }

    private boolean isAnswered(TelegramMessage message){
        return messageStorage.isAnswered(message.getMessageID());
    }
}
