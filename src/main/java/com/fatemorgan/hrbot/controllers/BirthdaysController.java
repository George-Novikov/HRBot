package com.fatemorgan.hrbot.controllers;

import com.fatemorgan.hrbot.network.Responder;
import com.fatemorgan.hrbot.services.BirthdaysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/birthdays")
public class BirthdaysController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BirthdaysController.class);
    private BirthdaysService service;

    public BirthdaysController(BirthdaysService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity getAllBirthdays(){
        try {
            return service.getBirthdays();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @GetMapping("/next")
    public ResponseEntity getNextBirthdays(){
        try {
            return service.getNextBirthdays();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }
}