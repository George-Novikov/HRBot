package com.georgen.hrbot.services;

import com.georgen.hrbot.model.constants.SystemMessage;
import com.georgen.hrbot.model.constants.TimerMessage;
import com.georgen.hrbot.model.settings.SettingsGlobalContainer;
import com.georgen.hrbot.network.Responder;
import com.georgen.hrbot.timers.BirthdaysTimer;
import com.georgen.hrbot.timers.ChatTimer;
import com.georgen.hrbot.timers.EventsTimer;
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
        if (isReadyToWork()) return Responder.sendError(SystemMessage.CHECK_EMPTY_SETTING);
        chatTimer.start();
        birthdaysTimer.start();
        eventsTimer.start();
        return Responder.sendOk(TimerMessage.ALL_TIMERS_START_MESSAGE);
    }

    public ResponseEntity startBirthdaysJob(){
        if (isReadyToWork()) return Responder.sendError(SystemMessage.CHECK_EMPTY_SETTING);
        return Responder.sendOk(birthdaysTimer.start());
    }

    public ResponseEntity startEventsJob(){
        if (isReadyToWork()) return Responder.sendError(SystemMessage.CHECK_EMPTY_SETTING);
        return Responder.sendOk(eventsTimer.start());
    }

    public ResponseEntity startChatJob(){
        if (isReadyToWork()) return Responder.sendError(SystemMessage.CHECK_EMPTY_SETTING);
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

    private boolean isReadyToWork(){
        return SettingsGlobalContainer.getInstance().isReadyToWork();
    }
}
