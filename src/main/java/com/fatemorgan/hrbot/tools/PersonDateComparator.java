package com.fatemorgan.hrbot.tools;

import com.fatemorgan.hrbot.model.birthdays.Person;
import com.fatemorgan.hrbot.model.settings.DateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;

public class PersonDateComparator implements Comparator<Person> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonDateComparator.class);
    private DateParser dateParser;

    public PersonDateComparator(DateParser dateParser) {
        this.dateParser = dateParser;
    }

    @Override
    public int compare(Person person1, Person person2) {
        try {
            Date date1 = dateParser.parse(person1.getBirthday());
            Date date2 = dateParser.parse(person2.getBirthday());

            if (date1 == null || date2 == null) return 0;

            return date1.compareTo(date2);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return 0;
        }
    }
}
