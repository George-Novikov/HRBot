package com.georgen.hrbot.model.exceptions;

public class DateParserException extends Exception {
    public DateParserException(){}

    public DateParserException(String message){
        super(message);
    }

    public DateParserException(String message, Throwable cause){
        super(message, cause);
    }
}
