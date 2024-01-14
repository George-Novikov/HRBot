package com.fatemorgan.hrbot.controllers;

import com.fatemorgan.hrbot.services.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemController.class);
    private SystemService service;

    public SystemController(SystemService service) {
        this.service = service;
    }

    @GetMapping("/settings")
    public ResponseEntity getSettings(){
        try {
            return service.getSettings();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity test(){
        try {
            return service.test();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
