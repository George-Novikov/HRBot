package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.model.DataGlobalContainer;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.constants.SystemMessage;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.exceptions.SettingsException;
import com.fatemorgan.hrbot.model.settings.*;
import com.fatemorgan.hrbot.network.Responder;
import com.fatemorgan.hrbot.storage.SettingsStorage;
import com.fatemorgan.hrbot.tools.datetime.Today;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SystemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemService.class);
    private GoogleSheetsService googleSheetsService;
    private DataGlobalContainer globalContainer;
    private SettingsStorage settingsStorage;

    public SystemService(GoogleSheetsService googleSheetsService,
                         DataGlobalContainer globalContainer,
                         SettingsStorage settingsStorage) {
        this.googleSheetsService = googleSheetsService;
        this.globalContainer = globalContainer;
        this.settingsStorage = settingsStorage;
    }

    public ResponseEntity getSettings(boolean isFull) throws IOException, SettingsException, DateParserException {
        googleSheetsService.updateGlobalContainer(isFull ? Action.ALL : Action.SETTINGS_UPDATE);

        DataSettings dataSettings = globalContainer.getSettings();
        if (dataSettings == null || dataSettings.isEmpty()) Responder.sendError(SystemMessage.SETTINGS_LOADING_ERROR);

        return Responder.sendOk(dataSettings);
    }

    public ResponseEntity getSettings() throws IOException, SettingsException, DateParserException {
        return Responder.sendOk(SettingsGlobalContainer.getInstance());
    }

    public ResponseEntity getToday() throws IOException, SettingsException, DateParserException {
        googleSheetsService.updateGlobalContainer(Action.SETTINGS_UPDATE);
        DataSettings dataSettings = globalContainer.getSettings();

        if (dataSettings == null || dataSettings.isEmpty()) Responder.sendError(SystemMessage.SETTINGS_LOADING_ERROR);

        return Responder.sendOk(Today.get());
    }

    public ResponseEntity saveSettings(Settings settings){
        settingsStorage.saveSettings(settings);
        return Responder.sendOk(SystemMessage.SETTINGS_SAVE_SUCCESS);
    }

    public ResponseEntity saveDataSettings(DataSettings dataSettings){
        Settings settings = settingsStorage.getSettings();
        settings.setDataSettings(dataSettings);
        settingsStorage.saveSettings(settings);
        return Responder.sendOk(SystemMessage.SETTINGS_SAVE_SUCCESS);
    }

    public ResponseEntity saveGoogleSettings(GoogleSettings googleSettings){
        Settings settings = settingsStorage.getSettings();
        settings.setGoogleSettings(googleSettings);
        settingsStorage.saveSettings(settings);
        return Responder.sendOk(SystemMessage.SETTINGS_SAVE_SUCCESS);
    }

    public ResponseEntity saveTelegramSettings(TelegramSettings telegramSettings){
        Settings settings = settingsStorage.getSettings();
        settings.setTelegramSettings(telegramSettings);
        settingsStorage.saveSettings(settings);
        return Responder.sendOk(SystemMessage.SETTINGS_SAVE_SUCCESS);
    }

    public ResponseEntity test() throws IOException, SettingsException, DateParserException {
        googleSheetsService.updateGlobalContainer(Action.ALL);
        return Responder.sendOk(SystemMessage.TEST_IS_OK);
    }
}
