package com.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentWeatherResponse {
    private double temp;
    private double feelsLike;
    private double windSpeed;
    private String windDirection;
    private int pressure;
    private int humidity;
    private String sunrise;
    private String sunset;
    private String icon;
    private String description;
    private String cityName;
    private String datetime;
}