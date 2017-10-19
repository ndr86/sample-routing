package com.mycompany.sample.routing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mycompany.sample.routing.controllers.CountryController;

@Configuration
public class AppConfig {

    @Bean
    public CountryController getCountryController() {
        return new CountryController();
    }
}
