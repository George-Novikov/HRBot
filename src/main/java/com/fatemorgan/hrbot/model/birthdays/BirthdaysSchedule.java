package com.fatemorgan.hrbot.model.birthdays;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fatemorgan.hrbot.model.constants.ColumnName;
import com.fatemorgan.hrbot.model.exceptions.DateParserException;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.serializers.JsonMaker;
import com.fatemorgan.hrbot.model.settings.DataSettings;
import com.fatemorgan.hrbot.model.settings.DateParser;
import com.fatemorgan.hrbot.model.settings.SettingsGlobalContainer;
import com.fatemorgan.hrbot.tools.comparators.PersonDateComparator;
import com.fatemorgan.hrbot.tools.SafeReader;
import com.fatemorgan.hrbot.tools.datetime.Today;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BirthdaysSchedule {
    private DateParser dateParser = SettingsGlobalContainer.getInstance().getDateParser();
    private String birthdayGreeting;
    private String nextBirthdayRequest;
    private List<Person> employees;
    public BirthdaysSchedule(SheetData sheet, DataSettings dataSettings) throws DateParserException {
        this.employees = new ArrayList<>();
        this.birthdayGreeting = dataSettings.getBirthdayGreeting();
        this.nextBirthdayRequest = dataSettings.getNextBirthdayRequest();
        fillEmployees(sheet, dataSettings);
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

    public List<Person> findCurrentBirthday(){
        if (this.employees.isEmpty()) return this.employees;
        Date today = Today.get();
        return filterByCurrentDate(today);
    }

    public List<Person> findNearestBirthday(DateParser dateParser) {
        if (this.employees.isEmpty()) return this.employees;
        Date today = Today.get();
        List<Person> nextBirthdays = filterByFutureDate(today);
        return nextBirthdays.isEmpty() ? nextBirthdays : findMostRelevant(nextBirthdays, dateParser);
    }

    private List<Person> filterByFutureDate(Date date){
        return this.employees
                .stream()
                .filter(person -> {
                    Date birthday = dateParser.parse(person.getBirthday());
                    return date.before(birthday);
                })
                .collect(Collectors.toList());
    }

    private List<Person> filterByCurrentDate(Date date){
        return this.employees
                .stream()
                .filter(person -> {
                    Date birthday = dateParser.parse(person.getBirthday());
                    return date.equals(birthday);
                })
                .collect(Collectors.toList());
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

    private void fillEmployees(SheetData sheet, DataSettings dataSettings) {
        if (sheet.isEmpty() || dataSettings.isEmpty()) return;

        int nameIndex = dataSettings.getColumnIndex(ColumnName.NAME.name());
        int birthdayIndex = dataSettings.getColumnIndex(ColumnName.BIRTHDAY.name());
        int nicknameIndex = dataSettings.getColumnIndex(ColumnName.NICKNAME.name());

        //TODO: null check

        for (List<String> row : sheet.getRows()){
            if (row.isEmpty()) continue;

            String name = getSafeValue(row, nameIndex);
            String birthday = getSafeValue(row, birthdayIndex);
            String nickname =  getSafeValue(row, nicknameIndex);

            if (dataSettings.isHeader(name) || dataSettings.isHeader(birthday) || dataSettings.isHeader(nickname)) continue;

            employees.add(new Person(name, birthday, nickname));
        }
    }

    private String getSafeValue(List<String> row, int index){
        return SafeReader.getValue(row, index);
    }

    public String toJson() throws JsonProcessingException {
        return JsonMaker.serialize(this);
    }

    public boolean isEmpty(){
        return this.employees == null || this.employees.isEmpty();
    }
}
