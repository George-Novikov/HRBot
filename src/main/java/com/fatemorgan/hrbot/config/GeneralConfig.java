package com.fatemorgan.hrbot.config;

import com.fatemorgan.hrbot.model.GlobalDataContainer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class GeneralConfig {
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Map<Long, Boolean> messageReplyBuffer(){
        return new HashMap();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public GlobalDataContainer globalDataContainer(){
        return new GlobalDataContainer();
    }
}
