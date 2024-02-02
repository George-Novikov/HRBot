package com.fatemorgan.hrbot.model.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatemorgan.hrbot.model.telegram.response.TelegramInfoResponse;
import com.fatemorgan.hrbot.model.telegram.response.messages.TelegramMessageResponse;

public class TelegramResponseSerializer {
    private static final ObjectMapper SERIALIZER = new ObjectMapper();
    private static final TypeReference<TelegramMessageResponse> MESSAGES_RESPONSE_TYPE = new TypeReference<TelegramMessageResponse>() {};

    private static final TypeReference<TelegramInfoResponse> INFO_RESPONSE_TYPE = new TypeReference<TelegramInfoResponse>() {};

    public static TelegramMessageResponse deserializeMessages(String json) throws JsonProcessingException {
        return SERIALIZER.readValue(json, MESSAGES_RESPONSE_TYPE);
    }

    public static TelegramInfoResponse deserializeInfo(String json) throws JsonProcessingException {
        return SERIALIZER.readValue(json, INFO_RESPONSE_TYPE);
    }
}
