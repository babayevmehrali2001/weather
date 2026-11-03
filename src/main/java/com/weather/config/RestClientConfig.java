package com.weather.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient owmRestClient(RestClient.Builder builder,
                                    @Value("${openweather.api.url}") String apiUrl) {
        return builder.baseUrl(apiUrl).build();
    }
}