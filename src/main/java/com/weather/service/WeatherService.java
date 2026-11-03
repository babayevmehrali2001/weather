package com.weather.service;

import com.weather.dto.CurrentWeatherResponse;
import com.weather.dto.OwmCurrentResponse;
import com.weather.exception.CityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestClient restClient;

    @Value("${openweather.api.key}")
    private String apiKey;

    public CurrentWeatherResponse getCurrentWeather(String city) {
        // 1) OWM-e sorgu gonderirik
        OwmCurrentResponse owm = restClient.get()
                .uri(uri -> uri.path("/weather")
                        .queryParam("q", city)
                        .queryParam("units", "metric")
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new CityNotFoundException("City '" + city + "' not found");
                })
                .body(OwmCurrentResponse.class);

        // 2) seherlerdeki saati duzelderik
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(owm.timezone());
        OwmCurrentResponse.Weather weather = owm.weather().get(0);

        // 3) sonra oz reponsumuzun icersin doldururq
        CurrentWeatherResponse result = new CurrentWeatherResponse();
        result.setTemp(owm.main().temp());
        result.setFeelsLike(owm.main().feelsLike());
        result.setWindSpeed(owm.wind().speed());
        result.setWindDirection(toDirection(owm.wind().deg()));
        result.setPressure(owm.main().pressure());
        result.setHumidity(owm.main().humidity());
        result.setSunrise(formatTime(owm.sys().sunrise(), offset, "HH:mm"));
        result.setSunset(formatTime(owm.sys().sunset(), offset, "HH:mm"));
        result.setIcon(weather.icon());
        result.setDescription(capitalize(weather.description()));
        result.setCityName(owm.name());
        result.setDatetime(formatTime(owm.dt(), offset, "yyyy-MM-dd HH:mm"));
        return result;
    }

    // dereceni istiqametlere veririk
    private String toDirection(double deg) {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        int index = (int) Math.round(deg / 45.0) % 8;
        return directions[index];
    }

    private String formatTime(long unixSeconds, ZoneOffset offset, String pattern) {
        return Instant.ofEpochSecond(unixSeconds)
                .atOffset(offset)
                .format(DateTimeFormatter.ofPattern(pattern));
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}