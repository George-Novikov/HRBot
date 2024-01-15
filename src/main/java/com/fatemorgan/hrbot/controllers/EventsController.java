package com.fatemorgan.hrbot.controllers;

import com.fatemorgan.hrbot.network.Responder;
import com.fatemorgan.hrbot.services.EventsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events")
public class EventsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsController.class);
    private EventsService service;

    public EventsController(EventsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity getEvents(){
        try {
            return service.getEvents();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @GetMapping("/next")
    public ResponseEntity getNextEvents(){
        try {
            return service.getNextEvents();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }
}
