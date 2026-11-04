package com.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyForecastResponse {
    private String dayName;
    private int minTemp;
    private int maxTemp;
    private String icon;
    private String description;
}