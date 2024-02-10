package com.georgen.hrbot.model.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMaker {
    private static final ObjectMapper SERIALIZER = new ObjectMapper();

    public static String serialize(Object object) throws JsonProcessingException {
        return SERIALIZER.writeValueAsString(object);
    }
}
