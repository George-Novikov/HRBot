package com.georgen.hrbot.model.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;

public class StringSetSerializer {
    private static final ObjectMapper SERIALIZER = new ObjectMapper();
    private static final TypeReference<Set<String>> STRING_SET_TYPE = new TypeReference<Set<String>>(){};

    public static String serialize(Set<String> list) throws JsonProcessingException {
        return SERIALIZER.writeValueAsString(list);
    }

    public static Set<String> deserialize(String json) throws JsonProcessingException {
        if (json == null || json.isEmpty()) return new HashSet<>();
        return SERIALIZER.readValue(json, STRING_SET_TYPE);
    }
}
