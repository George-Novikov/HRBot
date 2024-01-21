package com.fatemorgan.hrbot.model.exceptions;

import com.fatemorgan.hrbot.model.constants.Descriptive;

public class ChatException extends Exception {
    public ChatException(){}

    public ChatException(String message){
        super(message);
    }

    public ChatException(Descriptive descriptive){
        super(descriptive.describe());
    }

    public ChatException(String message, Throwable cause){
        super(message, cause);
    }
}
