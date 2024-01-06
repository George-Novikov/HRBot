package com.fatemorgan.hrbot.model.exceptions;

public class NetworkException extends Exception {
    public NetworkException(){}

    public NetworkException(String message){
        super(message);
    }

    public NetworkException(String message, Throwable cause){
        super(message, cause);
    }
}
