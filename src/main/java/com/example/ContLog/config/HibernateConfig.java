package com.example.ContLog.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return properties -> {
            properties.put("hibernate.jdbc.time_zone", "UTC");
            // Или для конкретного часового пояса:
//             properties.put("hibernate.jdbc.time_zone", "Europe/Moscow");
        };
    }
}
