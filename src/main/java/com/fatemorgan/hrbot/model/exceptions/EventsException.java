package com.fatemorgan.hrbot.model.exceptions;

import com.fatemorgan.hrbot.model.constants.Descriptive;

public class EventsException extends Exception {
    public EventsException(){}

    public EventsException(String message){
        super(message);
    }

    public EventsException(Descriptive descriptive){
        super(descriptive.describe());
    }

    public EventsException(String message, Throwable cause){
        super(message, cause);
    }
}
