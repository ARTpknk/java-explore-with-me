package ru.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.BaseClient;

@Configuration
public class StatsClientConfiguration {
    @Bean
    public BaseClient baseClient(@Value("http://localhost:9090") String statsServerUrl) {
        return new BaseClient(statsServerUrl);
    }
}