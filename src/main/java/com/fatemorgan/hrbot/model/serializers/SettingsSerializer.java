package com.fatemorgan.hrbot.model.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatemorgan.hrbot.model.settings.Settings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SettingsSerializer {
    private static final TypeReference<Settings> TYPE_REFERENCE = new TypeReference<Settings>(){};
    private ObjectMapper objectMapper;

    public SettingsSerializer(@Qualifier("settingsObjectMapper") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String serialize(Settings settings) throws JsonProcessingException {
        return objectMapper.writeValueAsString(settings);
    }

    public Settings deserialize(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, TYPE_REFERENCE);
    }
}
