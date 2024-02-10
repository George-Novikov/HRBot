package com.georgen.hrbot.network;

import com.georgen.hrbot.model.constants.Descriptive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Responder {
    public static ResponseEntity sendOk(Object object){
        return ResponseEntity.ok(object);
    }

    public static ResponseEntity sendOk(Descriptive descriptive){
        return ResponseEntity.ok(descriptive.describe());
    }

    public static ResponseEntity sendBadRequest(Descriptive descriptive){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(descriptive.describe());
    }

    public static ResponseEntity sendError(Descriptive descriptive){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(descriptive.describe());
    }

    public static ResponseEntity sendError(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
