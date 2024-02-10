package com.georgen.hrbot.services;

import com.georgen.hrbot.model.DataGlobalContainer;
import com.georgen.hrbot.model.constants.Action;
import com.georgen.hrbot.model.constants.EventsMessage;
import com.georgen.hrbot.model.constants.SystemMessage;
import com.georgen.hrbot.model.events.Event;
import com.georgen.hrbot.model.events.EventsSchedule;
import com.georgen.hrbot.model.exceptions.DateParserException;
import com.georgen.hrbot.model.exceptions.EventsException;
import com.georgen.hrbot.model.exceptions.SettingsException;
import com.georgen.hrbot.model.settings.DataSettings;
import com.georgen.hrbot.network.Responder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventsService {
    private GoogleSheetsService googleSheetsService;
    private DataGlobalContainer globalContainer;

    public EventsService(GoogleSheetsService googleSheetsService, DataGlobalContainer globalContainer) {
        this.googleSheetsService = googleSheetsService;
        this.globalContainer = globalContainer;
    }

    public ResponseEntity getEvents() throws IOException, SettingsException, DateParserException {
        this.googleSheetsService.updateGlobalContainer(Action.EVENTS_UPDATE);

        if (!globalContainer.hasEvents()) return Responder.sendError(EventsMessage.EVENTS_LOADING_ERROR);
        EventsSchedule events = globalContainer.getEvents();

        return Responder.sendOk(events);
    }

    public List<Event> getNextEvents() throws IOException, SettingsException, DateParserException, EventsException {
        initGlobalContainer();

        EventsSchedule events = globalContainer.getEvents();
        DataSettings dataSettings = globalContainer.getSettings();

        return events.findNextEvents(dataSettings.getDateParser());
    }

    public List<Event> getTodayEvents() throws IOException, SettingsException, DateParserException, EventsException {
        initGlobalContainer();

        EventsSchedule events = globalContainer.getEvents();
        DataSettings dataSettings = globalContainer.getSettings();

        return events.findTodayEvents(dataSettings.getDateParser());
    }

    public Set<String> extractEventNames(List<Event> events){
        return events.stream().map(person -> person.getDate()).collect(Collectors.toSet());
    }

    private void initGlobalContainer() throws IOException, SettingsException, DateParserException, EventsException {
        googleSheetsService.updateGlobalContainer(Action.EVENTS_UPDATE);
        if (!globalContainer.hasEvents()) throw new EventsException(EventsMessage.EVENTS_LOADING_ERROR);
        if (!globalContainer.hasSettings()) throw new SettingsException(SystemMessage.SETTINGS_LOADING_ERROR);
    }
}
