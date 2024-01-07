package com.fatemorgan.hrbot.model.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatemorgan.hrbot.model.telegram.request.TelegramRequest;

public class TelegramRequestSerializer {
    private static final ObjectMapper SERIALIZER = new ObjectMapper();
    private static final TypeReference<TelegramRequest> TYPE_REFERENCE = new TypeReference<TelegramRequest>() {};

    public static String serialize(TelegramRequest request) throws JsonProcessingException {
        return SERIALIZER.writeValueAsString(request);
    }
}
