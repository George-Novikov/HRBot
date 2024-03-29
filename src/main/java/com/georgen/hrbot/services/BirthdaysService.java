package com.georgen.hrbot.services;

import com.georgen.hrbot.model.DataGlobalContainer;
import com.georgen.hrbot.model.birthdays.BirthdaysSchedule;
import com.georgen.hrbot.model.birthdays.Person;
import com.georgen.hrbot.model.constants.Action;
import com.georgen.hrbot.model.constants.BirthdaysMessage;
import com.georgen.hrbot.model.constants.SystemMessage;
import com.georgen.hrbot.model.exceptions.BirthdaysException;
import com.georgen.hrbot.model.exceptions.DateParserException;
import com.georgen.hrbot.model.exceptions.SettingsException;
import com.georgen.hrbot.model.settings.DataSettings;
import com.georgen.hrbot.model.settings.DateParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BirthdaysService {
    private GoogleSheetsService sheetsService;
    private DataGlobalContainer globalContainer;

    public BirthdaysService(GoogleSheetsService sheetsService,
                               DataGlobalContainer globalContainer) {
        this.sheetsService = sheetsService;
        this.globalContainer = globalContainer;
    }

    public BirthdaysSchedule getBirthdays() throws IOException, SettingsException, DateParserException, BirthdaysException {
        initGlobalContainer();
        return globalContainer.getBirthdays();
    }

    public List<Person> getNextBirthdays() throws IOException, SettingsException, DateParserException, BirthdaysException {
        initGlobalContainer();

        BirthdaysSchedule birthdays = globalContainer.getBirthdays();
        DataSettings dataSettings = globalContainer.getSettings();

        DateParser dateParser = dataSettings.getDateParser();
        return birthdays.findNearestBirthday(dateParser);
    }

    public List<Person> getCurrentBirthdays() throws BirthdaysException, IOException, SettingsException, DateParserException {
        initGlobalContainer();
        BirthdaysSchedule birthdays = globalContainer.getBirthdays();
        return birthdays.findCurrentBirthday();
    }

    public List<String> getCurrentBirthdayWishes(List<Person> currentBirthdays){
        String greetingsSample = globalContainer.getSettings().getBirthdayGreeting();

        return currentBirthdays
                .stream()
                .map(person -> person.adaptGreetings(greetingsSample))
                .collect(Collectors.toList());
    }

    public Set<String> extractEventNames(List<Person> people){
        return people.stream().map(person -> person.getName()).collect(Collectors.toSet());
    }

    private void initGlobalContainer() throws IOException, SettingsException, DateParserException, BirthdaysException {
        sheetsService.updateGlobalContainer(Action.BIRTHDAYS_UPDATE);
        if (!globalContainer.hasBirthdays()) throw new BirthdaysException(BirthdaysMessage.BIRTHDAYS_LOADING_ERROR);
        if (!globalContainer.hasSettings()) throw new SettingsException(SystemMessage.SETTINGS_LOADING_ERROR);
    }
}
