package com.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RainProbabilityResponse {
    private String timeLabel;
    private int probability;
    private String condition;
}