package com.fatemorgan.hrbot.model.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatemorgan.hrbot.model.telegram.response.messages.TelegramMessageResponse;

public class TelegramResponseSerializer {
    private static final ObjectMapper SERIALIZER = new ObjectMapper();
    private static final TypeReference<TelegramMessageResponse> TYPE_REFERENCE = new TypeReference<TelegramMessageResponse>() {};

    public static TelegramMessageResponse deserialize(String json) throws JsonProcessingException {
        return SERIALIZER.readValue(json, TYPE_REFERENCE);
    }
}
