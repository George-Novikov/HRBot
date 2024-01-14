package com.fatemorgan.hrbot.controllers;

import com.fatemorgan.hrbot.model.GlobalDataContainer;
import com.fatemorgan.hrbot.model.birthdays.BirthdaysSchedule;
import com.fatemorgan.hrbot.model.constants.Action;
import com.fatemorgan.hrbot.model.settings.Settings;
import com.fatemorgan.hrbot.services.GoogleSheetsService;
import com.fatemorgan.hrbot.tools.Today;
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
    private GoogleSheetsService sheetsService;
    private GlobalDataContainer container;

    public BirthdaysController(GoogleSheetsService sheetsService,
                               GlobalDataContainer container) {
        this.sheetsService = sheetsService;
        this.container = container;
    }

    @GetMapping("/all")
    public ResponseEntity getAllBirthdays(){
        try {
            sheetsService.performSheetsAction(Action.BIRTHDAYS_UPDATE);
            BirthdaysSchedule birthdays = container.getBirthdays();

            if (birthdays == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No birthdays loaded");

            return ResponseEntity.ok(birthdays.getEmployees());
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/next")
    public ResponseEntity getNextBirthdays(){
        try {
            sheetsService.performSheetsAction(Action.BIRTHDAYS_UPDATE);
            BirthdaysSchedule birthdays = container.getBirthdays();
            Settings settings = container.getSettings();

            if (birthdays == null || settings == null || settings.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

            return ResponseEntity.ok(birthdays.findNext(settings.getDateParser()));
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getToday")
    public ResponseEntity getToday(){
        try {
            sheetsService.performSheetsAction(Action.SETTINGS_UPDATE);
            Settings settings = container.getSettings();

            if (settings == null || settings.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No settings loaded");

            return ResponseEntity.ok(Today.get());
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
