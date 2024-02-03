package com.fatemorgan.hrbot.model.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fatemorgan.hrbot.tools.LocaleParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

@JsonIgnoreProperties(value = {"getDateParser", "getZoneId", "getLocale"})
public class Settings {
    private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);

    private DataSettings dataSettings;
    private GoogleSettings googleSettings;
    private TelegramSettings telegramSettings;

    public Settings() {
        this.dataSettings = new DataSettings();
        this.googleSettings = new GoogleSettings();
        this.telegramSettings = new TelegramSettings();
    }

    public DataSettings getDataSettings() {
        if (this.dataSettings == null) return new DataSettings();
        return dataSettings;
    }

    public void setDataSettings(DataSettings dataSettings) {
        this.dataSettings = dataSettings;
    }

    public GoogleSettings getGoogleSettings() {
        if (this.googleSettings == null) return new GoogleSettings();
        return googleSettings;
    }

    public void setGoogleSettings(GoogleSettings googleSettings) {
        this.googleSettings = googleSettings;
    }

    public TelegramSettings getTelegramSettings() {
        if (this.telegramSettings == null) return new TelegramSettings();
        return telegramSettings;
    }

    public void setTelegramSettings(TelegramSettings telegramSettings) {
        this.telegramSettings = telegramSettings;
    }

    @JsonProperty("getDateParser")
    public DateParser getDateParser() {
        try {
            return this.getDataSettings().getDateParser();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return new DateParser();
        }
    }

    @JsonProperty("getZoneId")
    public ZoneId getZoneId(){
        String timeOffset = this.getDataSettings().getTimeOffset();
        return ZoneId.of(timeOffset);
    }

    @JsonProperty("getLocale")
    public Locale getLocale(){
        String localeString = this.getDataSettings().getLocale();
        return LocaleParser.parse(localeString);
    }

    public Map<String, Integer> getColumnsOrder(){
        return this.getDataSettings().getColumnsOrder();
    }
}
