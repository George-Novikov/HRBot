package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.model.GlobalDataContainer;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.constants.EventsMessage;
import com.fatemorgan.hrbot.model.constants.SystemMessage;
import com.fatemorgan.hrbot.model.events.EventsSchedule;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.exceptions.SettingsException;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.network.Responder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventsService {
    private GoogleSheetsService googleSheetsService;
    private GlobalDataContainer globalContainer;

    public EventsService(GoogleSheetsService googleSheetsService, GlobalDataContainer globalContainer) {
        this.googleSheetsService = googleSheetsService;
        this.globalContainer = globalContainer;
    }

    public ResponseEntity getEvents() throws IOException, SettingsException, DateParserException {
        this.googleSheetsService.updateGlobalContainer(Action.EVENTS_UPDATE);

        EventsSchedule events = globalContainer.getEvents();
        if (events == null) return Responder.sendError(EventsMessage.EVENTS_LOADING_ERROR);

        return Responder.sendOk(events);
    }

    public ResponseEntity getNextEvents() throws IOException, SettingsException, DateParserException {
        this.googleSheetsService.updateGlobalContainer(Action.EVENTS_UPDATE);

        EventsSchedule events = globalContainer.getEvents();
        Settings settings = globalContainer.getSettings();
        if (events == null) return Responder.sendError(EventsMessage.EVENTS_LOADING_ERROR);
        if (settings == null) return Responder.sendError(SystemMessage.SETTINGS_LOADING_ERROR);

        return Responder.sendOk(events.findNext(settings.getDateParser()));
    }
}
