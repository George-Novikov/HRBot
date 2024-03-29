package com.georgen.hrbot.model.exceptions;

import com.georgen.hrbot.model.constants.Descriptive;

public class SettingsException extends Exception {
    public SettingsException(){}

    public SettingsException(String message){
        super(message);
    }

    public SettingsException(Descriptive descriptive){
        super(descriptive.describe());
    }

    public SettingsException(String message, Throwable cause){
        super(message, cause);
    }
}
