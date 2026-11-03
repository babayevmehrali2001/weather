package com.weather.dto;

import java.util.List;

public record OwmCurrentResponse(
        long dt,
        int timezone,
        String name,
        Wind wind,
        Sys sys,
        Main main,
        List<Weather> weather
) {
    public record Wind(double speed, double deg) {}
    public record Sys(long sunrise, long sunset) {}
    public record Main(double temp, double feelsLike, int pressure, int humidity) {}
    public record Weather(String main, String description, String icon) {}
}