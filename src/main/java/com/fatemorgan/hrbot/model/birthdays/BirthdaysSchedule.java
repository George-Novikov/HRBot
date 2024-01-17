package com.fatemorgan.hrbot.model.birthdays;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fatemorgan.hrbot.model.constants.SettingsAttribute;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.serializers.JsonMaker;
import com.fatemorgan.hrbot.model.settings.DateParser;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.tools.comparators.PersonDateComparator;
import com.fatemorgan.hrbot.tools.SafeReader;
import com.fatemorgan.hrbot.tools.Today;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BirthdaysSchedule {
    private String birthdayGreeting;
    private String nextBirthdayRequest;
    private List<Person> employees;
    public BirthdaysSchedule(SheetData sheet, Settings settings) throws DateParserException {
        this.employees = new ArrayList<>();
        this.birthdayGreeting = settings.getBirthdayGreeting();
        this.nextBirthdayRequest = settings.getNextBirthdayRequest();
        fillEmployees(sheet, settings);
    }

    public String getBirthdayGreeting() {
        return birthdayGreeting;
    }

    public void setBirthdayGreeting(String birthdayGreeting) {
        this.birthdayGreeting = birthdayGreeting;
    }

    public String getNextBirthdayRequest() {
        return nextBirthdayRequest;
    }

    public void setNextBirthdayRequest(String nextBirthdayRequest) {
        this.nextBirthdayRequest = nextBirthdayRequest;
    }

    public List<Person> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Person> employees) {
        this.employees = employees;
    }

    public List<Person> findNearest(DateParser dateParser) {
        if (this.employees.isEmpty()) return this.employees;

        Date today = Today.get(dateParser);

        List<Person> nextBirthdays = this.employees
                .stream()
                .filter(person -> {
                    Date birthday = dateParser.parse(person.getBirthday());
                    return today.before(birthday);
                })
                .collect(Collectors.toList());

        return nextBirthdays.isEmpty() ? nextBirthdays : findMostRelevant(nextBirthdays, dateParser);
    }

    private List<Person> findMostRelevant(List<Person> nextBirthdays, DateParser dateParser){
        if (nextBirthdays == null || nextBirthdays.isEmpty()) return new ArrayList<>();
        Collections.sort(nextBirthdays, new PersonDateComparator(dateParser));
        Person nearest = nextBirthdays.get(0);
        return nextBirthdays
                .stream()
                .filter(person -> nearest.isEqualBirthday(person))
                .collect(Collectors.toList());
    }

    private void fillEmployees(SheetData sheet, Settings settings) {
        if (sheet.isEmpty() || settings.isEmpty()) return;

        int nameIndex = settings.getColumnIndex(SettingsAttribute.NAME);
        int birthdayIndex = settings.getColumnIndex(SettingsAttribute.BIRTHDAY);
        int nicknameIndex = settings.getColumnIndex(SettingsAttribute.NICKNAME);

        //TODO: null check

        for (List<String> row : sheet.getRows()){
            if (row.isEmpty()) continue;

            String name = getSafeValue(row, nameIndex);
            String birthday = getSafeValue(row, birthdayIndex);
            String nickname =  getSafeValue(row, nicknameIndex);

            if (settings.isHeader(name) || settings.isHeader(birthday) || settings.isHeader(nickname)) continue;

            employees.add(new Person(name, birthday, nickname));
        }
    }

    private String getSafeValue(List<String> row, int index){
        return SafeReader.getValue(row, index);
    }

    public String toJson() throws JsonProcessingException {
        return JsonMaker.serialize(this);
    }
}
