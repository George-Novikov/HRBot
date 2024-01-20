package com.fatemorgan.hrbot.model.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

public class StringListSerializer {
    private static final ObjectMapper SERIALIZER = new ObjectMapper();
    private static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<List<String>>(){};

    public static String serialize(List<String> list) throws JsonProcessingException {
        return SERIALIZER.writeValueAsString(list);
    }

    public static List<String> deserialize(String json) throws JsonProcessingException {
        return SERIALIZER.readValue(json, STRING_LIST_TYPE);
    }
}
