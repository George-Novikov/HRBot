package com.fatemorgan.hrbot.workers;

import com.fatemorgan.hrbot.workers.timers.BirthdaysTimer;
import com.fatemorgan.hrbot.workers.timers.ChatTimer;
import com.fatemorgan.hrbot.workers.timers.EventsTimer;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class TimersHandler {
    private BirthdaysTimer birthdaysTimer;
    private EventsTimer eventsTimer;
    private ChatTimer chatTimer;

    public TimersHandler(
            BirthdaysTimer birthdaysTimer,
            EventsTimer eventsTimer,
            ChatTimer chatTimer
    ) {
        this.birthdaysTimer = birthdaysTimer;
        this.eventsTimer = eventsTimer;
        this.chatTimer = chatTimer;
    }

    public void init(){

        birthdaysTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Your database code here
            }
        }, 2*60*1000, 2*60*1000);
    }
}
