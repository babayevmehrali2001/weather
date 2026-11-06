package com.weather.controller;

import com.weather.swagger.WeatherApi;
import com.weather.dto.ApiResponse;
import com.weather.dto.CurrentWeatherResponse;
import com.weather.dto.DailyForecastResponse;
import com.weather.dto.RainProbabilityResponse;
import com.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController implements WeatherApi {

    private final WeatherService weatherService;

    @Override
    @GetMapping("/current")
    public ApiResponse<CurrentWeatherResponse> getCurrent(@RequestParam String city) {
        return new ApiResponse<>(true, weatherService.getCurrentWeather(city), "OK");
    }

    @Override
    @GetMapping("/forecast")
    public ApiResponse<List<DailyForecastResponse>> getForecast(@RequestParam String city) {
        return new ApiResponse<>(true, weatherService.getWeeklyForecast(city), "OK");
    }

    @Override
    @GetMapping("/rain-probability")
    public ApiResponse<List<RainProbabilityResponse>> getRainProbability(@RequestParam String city) {
        return new ApiResponse<>(true, weatherService.getRainProbability(city), "OK");
    }
}