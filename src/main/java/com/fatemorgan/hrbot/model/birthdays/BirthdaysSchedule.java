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

import java.text.ParseException;
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

    public List<Person> findNext(DateParser dateParser) {
        if (this.employees.isEmpty()) return this.employees;

        Date today = Today.get(dateParser);
        Collections.sort(this.employees, new PersonDateComparator(dateParser));

        List<Person> nextBirthdays = new ArrayList<>();
        for (Person person : this.employees){
            Date birthday = dateParser.parse(person.getBirthday());
            if (today.before(birthday)) nextBirthdays.add(person);
        }

        return nextBirthdays;
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
