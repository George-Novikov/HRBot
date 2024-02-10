package com.georgen.hrbot.model.exceptions;

import com.georgen.hrbot.model.constants.Descriptive;

public class BirthdaysException extends Exception {
    public BirthdaysException(){}

    public BirthdaysException(String message){
        super(message);
    }

    public BirthdaysException(Descriptive descriptive){
        super(descriptive.describe());
    }

    public BirthdaysException(String message, Throwable cause){
        super(message, cause);
    }
}
