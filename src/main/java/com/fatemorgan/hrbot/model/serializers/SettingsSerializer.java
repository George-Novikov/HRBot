package com.fatemorgan.hrbot.model.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatemorgan.hrbot.model.settings.Settings;

public class SettingsSerializer {
    private static final ObjectMapper SERIALIZER = new ObjectMapper();

    private static final TypeReference<Settings> TYPE_REFERENCE = new TypeReference<Settings>(){};

    public static String serialize(Settings settings) throws JsonProcessingException {
        return SERIALIZER.writeValueAsString(settings);
    }

    public static Settings deserialize(String json) throws JsonProcessingException {
        return SERIALIZER.readValue(json, TYPE_REFERENCE);
    }
}
