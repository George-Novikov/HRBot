package com.georgen.hrbot.controllers;

import com.georgen.hrbot.model.settings.Settings;
import com.georgen.hrbot.network.Responder;
import com.georgen.hrbot.services.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemController.class);
    private SystemService service;

    public SystemController(SystemService service) {
        this.service = service;
    }

    @GetMapping("/settings")
    public ResponseEntity getSettings(
            @RequestParam(value = "isFull", defaultValue = "false") boolean isFull
    ){
        try {
            return service.getSettings();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @PostMapping("/settings")
    public ResponseEntity saveSettings(@RequestBody Settings settings){
        try {
            return service.saveSettings(settings);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @GetMapping("/test")
    public ResponseEntity test(){
        try {
            return service.test();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    @GetMapping("/getToday")
    public ResponseEntity getToday(){
        try {
            return service.getToday();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return Responder.sendError(e);
        }
    }

    //TODO: getAvailableTimezones();
}
