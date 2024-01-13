package com.fatemorgan.hrbot.model.exceptions;

public class SheetsException extends Exception {
    public SheetsException(){}

    public SheetsException(String message){
        super(message);
    }

    public SheetsException(String message, Throwable cause){
        super(message, cause);
    }
}
