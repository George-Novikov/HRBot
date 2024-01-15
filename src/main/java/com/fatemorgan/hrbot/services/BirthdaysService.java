package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.model.GlobalDataContainer;
import com.fatemorgan.hrbot.model.birthdays.BirthdaysSchedule;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.constants.BirthdaysMessage;
import com.fatemorgan.hrbot.model.constants.SystemMessage;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.exceptions.SettingsException;
import com.fatemorgan.hrbot.model.settings.DateParser;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.network.Responder;
import com.fatemorgan.hrbot.tools.Today;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BirthdaysService {
    private GoogleSheetsService sheetsService;
    private GlobalDataContainer globalContainer;

    public BirthdaysService(GoogleSheetsService sheetsService,
                               GlobalDataContainer globalContainer) {
        this.sheetsService = sheetsService;
        this.globalContainer = globalContainer;
    }

    public ResponseEntity getBirthdays() throws IOException, SettingsException, DateParserException {
        sheetsService.updateGlobalContainer(Action.BIRTHDAYS_UPDATE);
        BirthdaysSchedule birthdays = globalContainer.getBirthdays();

        if (birthdays == null) return Responder.sendError(BirthdaysMessage.BIRTHDAYS_LOADING_ERROR);

        return Responder.sendOk(birthdays);
    }

    public ResponseEntity getNextBirthdays() throws IOException, SettingsException, DateParserException {
        sheetsService.updateGlobalContainer(Action.BIRTHDAYS_UPDATE);
        BirthdaysSchedule birthdays = globalContainer.getBirthdays();
        Settings settings = globalContainer.getSettings();

        if (birthdays == null) return Responder.sendError(BirthdaysMessage.BIRTHDAYS_LOADING_ERROR);
        if (settings == null || settings.isEmpty()) Responder.sendError(SystemMessage.SETTINGS_LOADING_ERROR);

        DateParser dateParser = settings.getDateParser();
        return Responder.sendOk(birthdays.findNearest(dateParser));
    }
}
