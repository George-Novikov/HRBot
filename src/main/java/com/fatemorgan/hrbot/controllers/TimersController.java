package com.fatemorgan.hrbot.controllers;

import com.fatemorgan.hrbot.model.constants.TimerMessage;
import com.fatemorgan.hrbot.network.Responder;
import com.fatemorgan.hrbot.services.TimersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/timers")
public class TimersController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimersController.class);
    private TimersService timersService;

    public TimersController(TimersService timersService) {
        this.timersService = timersService;
    }

    @PostMapping("/all/start")
    public ResponseEntity startAllJobs(){
        try {
            return timersService.startAllJobs();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @PostMapping("/birthdays/start")
    public ResponseEntity startBirthdaysJob(){
        try {
            return timersService.startBirthdaysJob();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @PostMapping("/events/start")
    public ResponseEntity startEventsJob(){
        try {
            return timersService.startEventsJob();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @PostMapping("/chat/start")
    public ResponseEntity startChatJob(){
        try {
            return timersService.startChatJob();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @PostMapping("/all/stop")
    public ResponseEntity stopAllJobs(){
        try {
            return timersService.stopAllJobs();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @PostMapping("/birthdays/stop")
    public ResponseEntity stopBirthdaysJob(){
        try {
            return timersService.stopBirthdaysJob();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @PostMapping("/events/stop")
    public ResponseEntity stopEventsJob(){
        try {
            return timersService.stopEventsJob();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @PostMapping("/chat/stop")
    public ResponseEntity stopChatJob(){
        try {
            return timersService.stopChatJob();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }
}
