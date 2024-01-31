package com.fatemorgan.hrbot.model.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatemorgan.hrbot.model.telegram.request.InlineButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InlineButtonSerializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(InlineButtonSerializer.class);
    private static final ObjectMapper SERIALIZER = new ObjectMapper();
    private static final TypeReference<List<InlineButton>> TYPE_REFERENCE = new TypeReference<List<InlineButton>>(){};

    public static List<InlineButton> deserialize(String json) {
        try {
            return SERIALIZER.readValue(json, TYPE_REFERENCE);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
