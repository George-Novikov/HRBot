package com.fatemorgan.hrbot.services;

import com.fatemorgan.hrbot.model.constants.TimerMessage;
import com.fatemorgan.hrbot.network.Responder;
import com.fatemorgan.hrbot.timers.BirthdaysTimer;
import com.fatemorgan.hrbot.timers.ChatTimer;
import com.fatemorgan.hrbot.timers.EventsTimer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TimersService {
    private ChatTimer chatTimer;
    private BirthdaysTimer birthdaysTimer;
    private EventsTimer eventsTimer;

    public TimersService(ChatTimer chatTimer, BirthdaysTimer birthdaysTimer, EventsTimer eventsTimer) {
        this.chatTimer = chatTimer;
        this.birthdaysTimer = birthdaysTimer;
        this.eventsTimer = eventsTimer;
    }

    public ResponseEntity startAllJobs(){
        chatTimer.start();
        birthdaysTimer.start();
        eventsTimer.start();
        return Responder.sendOk(TimerMessage.ALL_TIMERS_START_MESSAGE);
    }

    public ResponseEntity startBirthdaysJob(){
        return Responder.sendOk(birthdaysTimer.start());
    }

    public ResponseEntity startEventsJob(){
        return Responder.sendOk(eventsTimer.start());
    }

    public ResponseEntity startChatJob(){
        return Responder.sendOk(chatTimer.start());
    }

    public ResponseEntity stopAllJobs(){
        stopBirthdaysTimer();
        stopEventsTimer();
        stopChatTimer();
        return Responder.sendOk(TimerMessage.ALL_TIMERS_STOP_MESSAGE);
    }

    public ResponseEntity stopBirthdaysJob(){
        stopBirthdaysTimer();
        return Responder.sendOk(TimerMessage.BIRTHDAYS_TIMER_STOP_MESSAGE);
    }

    public ResponseEntity stopEventsJob(){
        stopEventsTimer();
        return Responder.sendOk(TimerMessage.EVENTS_TIMER_STOP_MESSAGE);
    }

    public ResponseEntity stopChatJob(){
        stopChatTimer();
        return Responder.sendOk(TimerMessage.CHATS_TIMER_STOP_MESSAGE);
    }

    private void stopBirthdaysTimer(){
        birthdaysTimer.cancel();
        birthdaysTimer.purge();
    }

    private void stopEventsTimer(){
        eventsTimer.cancel();
        chatTimer.purge();
    }

    private void stopChatTimer(){
        chatTimer.cancel();
        chatTimer.purge();
    }
}
