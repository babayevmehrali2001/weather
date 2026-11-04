package com.weather.controller;

import com.weather.dto.ApiResponse;
import com.weather.dto.CurrentWeatherResponse;
import com.weather.dto.DailyForecastResponse;
import com.weather.dto.RainProbabilityResponse;
import com.weather.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/forecast")
    public ApiResponse<List<DailyForecastResponse>> getForecast(@RequestParam String city) {
        return new ApiResponse<>(true, weatherService.getWeeklyForecast(city), "OK");
    }

    @GetMapping("/rain-probability")
    public ApiResponse<List<RainProbabilityResponse>> getRainProbability(@RequestParam String city) {
        return new ApiResponse<>(true, weatherService.getRainProbability(city), "OK");
    }
}