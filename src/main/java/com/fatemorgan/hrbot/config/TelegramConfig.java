package com.fatemorgan.hrbot.config;

import com.fatemorgan.hrbot.storage.MessageStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramConfig {
    @Value("${telegram.bot-name}")
    private String botName;

    @Bean
    @Qualifier("botNickName")
    public String getBotNickname(){
        return String.format("@%s", botName);
    }
}
