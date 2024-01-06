package com.fatemorgan.hrbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HrBotApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HrBotApplication.class, args);
    }
}
