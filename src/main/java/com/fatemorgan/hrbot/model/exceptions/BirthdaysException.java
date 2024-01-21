package com.fatemorgan.hrbot.model.exceptions;

import com.fatemorgan.hrbot.model.constants.Descriptive;

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
