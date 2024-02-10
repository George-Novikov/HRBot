package com.georgen.hrbot.model.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LongSetSerializer {
    private static final ObjectMapper SERIALIZER = new ObjectMapper();
    private static final TypeReference<Set<Long>> LONG_LIST_TYPE = new TypeReference<Set<Long>>(){};

    public static String serialize(Set<Long> list) throws JsonProcessingException {
        return SERIALIZER.writeValueAsString(list);
    }

    public static Set<Long> deserialize(String json) throws JsonProcessingException {
        if (json == null || json.isEmpty()) return new HashSet<>();
        return SERIALIZER.readValue(json, LONG_LIST_TYPE);
    }
}
