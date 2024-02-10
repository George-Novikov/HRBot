package com.georgen.hrbot.model.constants;

public enum TimerMessage implements Descriptive {
    ALL_TIMERS_START_MESSAGE("All timers have started."),
    ALL_TIMERS_STOP_MESSAGE("All timers have stopped."),
    BIRTHDAYS_TIMER_STOP_MESSAGE("The birthday timer has stopped."),
    EVENTS_TIMER_STOP_MESSAGE("The event timer has stopped."),
    CHATS_TIMER_STOP_MESSAGE("The chat timer has stopped.");

    private String description;

    TimerMessage(String description) {
        this.description = description;
    }

    @Override
    public String describe() {
        return null;
    }
}
