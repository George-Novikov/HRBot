package com.georgen.hrbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HrBotApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HrBotApplication.class, args);
    }
}
