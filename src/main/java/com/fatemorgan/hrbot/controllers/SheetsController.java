package com.fatemorgan.hrbot.controllers;

import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.services.GoogleSheetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sheets")
public class SheetsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SheetsController.class);
    private GoogleSheetsService googleSheetsService;

    public SheetsController(GoogleSheetsService googleSheetsService) {
        this.googleSheetsService = googleSheetsService;
    }

    @GetMapping("/test")
    public ResponseEntity test(){
        try {
            googleSheetsService.getSheetsData(Action.SETTINGS_UPDATE);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
