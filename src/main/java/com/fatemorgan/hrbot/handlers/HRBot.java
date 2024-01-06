package com.fatemorgan.hrbot.handlers;

import com.fatemorgan.hrbot.model.constants.ApiParam;
import com.fatemorgan.hrbot.network.HttpConnector;
import com.fatemorgan.hrbot.network.UrlParamBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class HRBot{

    @Value("${bot.chat-id}")
    private Long chatID;

    public void sendMessage(String message) throws Exception {
        try (HttpConnector connector = new HttpConnector()){
            String response = connector.get("/sendMessage", buildMessageParams(message));
        }
    }

    private String buildMessageParams(String message){
        return new UrlParamBuilder()
                .add(ApiParam.CHAT_ID, chatID.toString())
                .add(ApiParam.TEXT, message)
                .build();
    }
}
