package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.model.GlobalDataContainer;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.constants.SystemMessage;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.exceptions.SettingsException;
import com.fatemorgan.hrbot.model.settings.DataSettings;
import com.fatemorgan.hrbot.network.Responder;
import com.fatemorgan.hrbot.tools.datetime.Today;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SystemService {
    private GoogleSheetsService googleSheetsService;
    private GlobalDataContainer globalContainer;

    public SystemService(GoogleSheetsService googleSheetsService,
                         GlobalDataContainer globalContainer) {
        this.googleSheetsService = googleSheetsService;
        this.globalContainer = globalContainer;
    }

    public ResponseEntity getSettings(boolean isFull) throws IOException, SettingsException, DateParserException {
        googleSheetsService.updateGlobalContainer(isFull ? Action.ALL : Action.SETTINGS_UPDATE);

        DataSettings dataSettings = globalContainer.getSettings();
        if (dataSettings == null || dataSettings.isEmpty()) Responder.sendError(SystemMessage.SETTINGS_LOADING_ERROR);

        return Responder.sendOk(dataSettings);
    }

    public ResponseEntity getToday() throws IOException, SettingsException, DateParserException {
        googleSheetsService.updateGlobalContainer(Action.SETTINGS_UPDATE);
        DataSettings dataSettings = globalContainer.getSettings();

        if (dataSettings == null || dataSettings.isEmpty()) Responder.sendError(SystemMessage.SETTINGS_LOADING_ERROR);

        return Responder.sendOk(Today.get());
    }

    public ResponseEntity test() throws IOException, SettingsException, DateParserException {
        googleSheetsService.updateGlobalContainer(Action.ALL);
        return Responder.sendOk(SystemMessage.TEST_IS_OK);
    }
}
