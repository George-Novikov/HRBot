package com.fatemorgan.hrbot.model.birthdays;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fatemorgan.hrbot.model.constants.SettingsAttribute;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.serializers.JsonMaker;
import com.fatemorgan.hrbot.model.settings.DateParser;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.tools.PersonDateComparator;
import com.fatemorgan.hrbot.tools.SafeReader;
import com.fatemorgan.hrbot.tools.Today;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BirthdaysSchedule {
    private List<Person> employees;
    public BirthdaysSchedule(SheetData sheet, Settings settings) throws DateParserException {
        this.employees = new ArrayList<>();
        fillEmployees(sheet, settings);
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

    private void fillEmployees(SheetData sheet, Settings settings) throws DateParserException {
        if (sheet.isEmpty() || settings.isEmpty()) return;

        int nameIndex = settings.getColumnIndex(SettingsAttribute.NAME);
        int birthdayIndex = settings.getColumnIndex(SettingsAttribute.BIRTHDAY);
        int nicknameIndex = settings.getColumnIndex(SettingsAttribute.NICKNAME);

        for (List<String> row : sheet.getRows()){
            if (row.isEmpty()) continue;

            employees.add(
                    new Person(
                            getSafeValue(row, nameIndex),
                            getSafeValue(row, birthdayIndex),
                            getSafeValue(row, nicknameIndex)
                    )
            );
        }
    }

    private String getSafeValue(List<String> row, int index){
        return SafeReader.getValue(row, index);
    }

    public String toJson() throws JsonProcessingException {
        return JsonMaker.serialize(this);
    }
}
