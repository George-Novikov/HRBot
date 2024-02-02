package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.model.GlobalDataContainer;
import com.fatemorgan.hrbot.model.birthdays.BirthdaysSchedule;
import com.fatemorgan.hrbot.model.birthdays.Person;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.constants.BirthdaysMessage;
import com.fatemorgan.hrbot.model.constants.SystemMessage;
import com.fatemorgan.hrbot.model.exceptions.BirthdaysException;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.exceptions.SettingsException;
import com.fatemorgan.hrbot.model.settings.DataSettings;
import com.fatemorgan.hrbot.model.settings.DateParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BirthdaysService {
    private GoogleSheetsService sheetsService;
    private GlobalDataContainer globalContainer;

    public BirthdaysService(GoogleSheetsService sheetsService,
                               GlobalDataContainer globalContainer) {
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
        DataSettings dataSettings = globalContainer.getSettings();

        DateParser dateParser = dataSettings.getDateParser();
        return birthdays.findCurrentBirthday(dateParser);
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
