package com.fatemorgan.hrbot.workers;

import com.fatemorgan.hrbot.model.birthdays.BirthdaysSchedule;
import com.fatemorgan.hrbot.model.settings.Settings;

public class BirthdaysHandler {
    private BirthdaysSchedule schedule;
    private Settings settings;

    public BirthdaysHandler(BirthdaysSchedule schedule, Settings settings) {
        this.schedule = schedule;
        this.settings = settings;
    }


}
