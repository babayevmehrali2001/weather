package com.weather.dto;

import java.util.List;

public record OwmForecastResponse(
        List<Item> list,
        City city
) {
    public record Item(long dt, Main main, List<Weather> weather, double pop) {}
    public record Main(double tempMin, double tempMax) {}
    public record Weather(String main, String description, String icon) {}
    public record City(String name, int timezone) {}
}