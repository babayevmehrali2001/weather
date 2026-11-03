package com.weather.controller;

import com.weather.dto.ApiResponse;
import com.weather.dto.CurrentWeatherResponse;
import com.weather.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current")
    public ApiResponse<CurrentWeatherResponse> getCurrent(@RequestParam String city) {
        CurrentWeatherResponse data = weatherService.getCurrentWeather(city);
        return new ApiResponse<>(true, data, "OK");
    }
}