package com.georgen.hrbot.model.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.georgen.hrbot.model.telegram.request.TelegramMessageRequest;

public class TelegramRequestSerializer {
    private static final ObjectMapper SERIALIZER = new ObjectMapper();
    private static final TypeReference<TelegramMessageRequest> TYPE_REFERENCE = new TypeReference<TelegramMessageRequest>() {};

    public static String serialize(TelegramMessageRequest request) throws JsonProcessingException {
        return SERIALIZER.writeValueAsString(request);
    }
}
