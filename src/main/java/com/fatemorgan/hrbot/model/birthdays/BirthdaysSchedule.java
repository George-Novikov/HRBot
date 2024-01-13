package com.fatemorgan.hrbot.model.birthdays;

import com.fatemorgan.hrbot.model.FieldsOrder;
import com.fatemorgan.hrbot.model.google.SheetData;
import com.fatemorgan.hrbot.model.settings.Settings;

import java.text.DateFormat;
import java.util.List;

public class BirthdaysSchedule {
    private DateFormat dateFormat;
    private FieldsOrder fieldsOrder;
    private List<Person> employees;
    public BirthdaysSchedule(SheetData sheet, Settings settings){
        this.dateFormat = settings.getDateParser();
    }
}
