package com.fatemorgan.hrbot.config;

import com.fatemorgan.hrbot.model.DataGlobalContainer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class GeneralConfig {
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public DataGlobalContainer globalDataContainer(){
        return new DataGlobalContainer();
    }

    public static boolean isUnixSystem(){
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("nix") || osName.contains("nux");
    }
}
