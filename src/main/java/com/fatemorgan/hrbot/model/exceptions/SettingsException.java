package com.fatemorgan.hrbot.model.exceptions;

public class SettingsException extends Exception {
    public SettingsException(){}

    public SettingsException(String message){
        super(message);
    }

    public SettingsException(String message, Throwable cause){
        super(message, cause);
    }
}
