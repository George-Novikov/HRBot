package com.fatemorgan.hrbot.handlers;

import com.fatemorgan.hrbot.config.TelegramConfig;
import com.fatemorgan.hrbot.model.constants.ApiParam;
import com.fatemorgan.hrbot.network.HttpConnector;
import com.fatemorgan.hrbot.network.UrlParamBuilder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class HRBot{
    private TelegramConfig config;

    public HRBot(TelegramConfig config) {
        this.config = config;
    }

    public String sendMessage(String message) throws Exception {
        try (HttpConnector connector = new HttpConnector(config)){
            return connector.get("/sendMessage", buildMessageParams(message));
        }
    }

    private String buildMessageParams(String message) throws UnsupportedEncodingException {
        return new UrlParamBuilder()
                .add(ApiParam.CHAT_ID, config.getChatID().toString())
                .add(ApiParam.TEXT, URLEncoder.encode(message, "UTF-8"))
                .build();
    }
}
