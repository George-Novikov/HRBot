package com.georgen.hrbot.model.exceptions;

import com.georgen.hrbot.model.constants.Descriptive;

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
