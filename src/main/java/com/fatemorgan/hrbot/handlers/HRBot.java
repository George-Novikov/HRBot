package com.fatemorgan.hrbot.handlers;

import com.fatemorgan.hrbot.config.TelegramConfig;
import com.fatemorgan.hrbot.model.constants.TelegramApiParam;
import com.fatemorgan.hrbot.model.serializers.TelegramRequestSerializer;
import com.fatemorgan.hrbot.model.serializers.TelegramResponseSerializer;
import com.fatemorgan.hrbot.model.telegram.request.TelegramRequest;
import com.fatemorgan.hrbot.model.telegram.response.TelegramMessage;
import com.fatemorgan.hrbot.model.telegram.response.TelegramResponse;
import com.fatemorgan.hrbot.network.HttpConnector;
import com.fatemorgan.hrbot.network.UrlParamBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HRBot{
    private static final Logger LOGGER = LoggerFactory.getLogger(HRBot.class);
    private TelegramConfig config;
    private Map<Long, Boolean> messageReplyBuffer;

    public HRBot(TelegramConfig config, Map<Long, Boolean> messageReplyBuffer) {
        this.config = config;
        this.messageReplyBuffer = messageReplyBuffer;
    }

    public String sendMessage(String message) throws Exception {
        try (HttpConnector connector = new HttpConnector(config)){
            return connector.get("/sendMessage", buildMessageParams(message));
        }
    }

    public String reply(String message, Long repliedMessageID) throws Exception {
        try (HttpConnector connector = new HttpConnector(config)){
            TelegramRequest request = new TelegramRequest(message, repliedMessageID);
            return connector.post(
                    "/sendMessage",
                    buildMessageParams(message),
                    TelegramRequestSerializer.serialize(request)
            );
        }
    }

    public TelegramResponse getUpdates() {
        TelegramResponse response = null;

        try (HttpConnector connector = new HttpConnector(config)){
            String jsonResponse = connector.get("/getUpdates");
            if (jsonResponse != null) response = TelegramResponseSerializer.deserialize(jsonResponse);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        }

        if (response != null) response.cleanUpResiduals();

        return response;
    }

    public String replyUnanswered() throws Exception {
        TelegramResponse response = getUpdates();
        if (response == null || response.getResult() == null) return "No updates";

        List<TelegramMessage> messages = response.getResult()
                .stream()
                .filter(result -> result.getMessage() != null)
                .map(result -> result.getMessage())
                .collect(Collectors.toList());

        for (TelegramMessage message : messages){
            Boolean isAnswered = messageReplyBuffer.get(message.getMessageID());

            if (!Boolean.TRUE.equals(isAnswered)){
                String jsonResponse = reply("Reply", message.getMessageID());

                if (jsonResponse != null) messageReplyBuffer.put(message.getMessageID(), true);
            }
        }

        return messageReplyBuffer.toString();
    }

    private String buildMessageParams(String message) throws UnsupportedEncodingException {
        return new UrlParamBuilder()
                .add(TelegramApiParam.CHAT_ID, config.getChatID().toString())
                .add(TelegramApiParam.TEXT, URLEncoder.encode(message, "UTF-8"))
                .build();
    }
}
