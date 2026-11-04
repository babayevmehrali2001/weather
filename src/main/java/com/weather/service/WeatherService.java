package com.weather.service;

import com.weather.dto.*;
import com.weather.exception.CityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestClient restClient;

    @Value("${openweather.api.key}")
    private String apiKey;

    public CurrentWeatherResponse getCurrentWeather(String city) {
        OwmCurrentResponse owm = fetchCurrent(city);   // Owmddeki umumi datani getiririk

        CurrentWeatherResponse result = new CurrentWeatherResponse();   // sonra oz responsumuz bos obj yaradiriq

        // 3) set olunaraq bizim res doldurulur
        result.setTemp(owm.main().temp());
        result.setFeelsLike(owm.main().feelsLike());
        result.setWindSpeed(owm.wind().speed());
        result.setPressure(owm.main().pressure());
        result.setHumidity(owm.main().humidity());
        result.setIcon(owm.weather().get(0).icon());
        result.setCityName(owm.name());

        // vaxti san seklinde verir deyisirik
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(owm.timezone());

        result.setWindDirection(toDirection(owm.wind().deg()));
        result.setSunrise(formatTime(owm.sys().sunrise(), offset, "HH:mm"));
        result.setSunset(formatTime(owm.sys().sunset(), offset, "HH:mm"));
        result.setDatetime(formatTime(owm.dt(), offset, "yyyy-MM-dd HH:mm"));
        result.setDescription(capitalize(owm.weather().get(0).description()));

        // seliqeye saldigimiz res qaytarirq
        return result;
    }

    // her hansi seherin melumatin getir
    private OwmCurrentResponse fetchCurrent(String city) {
        return restClient.get()
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
    }

    // derece seklinde gelir ist veririk
    private String toDirection(double deg) {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        int index = (int) Math.round(deg / 45.0) % 8;
        return directions[index];
    }

    // vaxt duzeldirik
    private String formatTime(long unixSeconds, ZoneOffset offset, String pattern) {
        return Instant.ofEpochSecond(unixSeconds)
                .atOffset(offset)
                .format(DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH));
    }

    // taskda Suuny Cloudy cixsin deyilir
    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }



    // 2-ci endpointe kecirik

    private OwmForecastResponse fetchForecast(String city) {
        return restClient.get()
                .uri(uri -> uri.path("/forecast")
                        .queryParam("q", city)
                        .queryParam("units", "metric")
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new CityNotFoundException("City '" + city + "' not found");
                })
                .body(OwmForecastResponse.class);
    }

    // heftelik proqnoz
    public List<DailyForecastResponse> getWeeklyForecast(String city) {
        OwmForecastResponse owm = fetchForecast(city);   // owm-deki melumati getir
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(owm.city().timezone());
        Map<LocalDate, List<OwmForecastResponse.Item>> days = groupByDay(owm, offset);  // gunlere bol

        List<DailyForecastResponse> result = new ArrayList<>();  // bos obj yaradiriq

        for (Map.Entry<LocalDate, List<OwmForecastResponse.Item>> day : days.entrySet()) {
            LocalDate date = day.getKey();
            List<OwmForecastResponse.Item> items = day.getValue();

            // a) min və max temperaturu tapiriq
            double min = items.get(0).main().tempMin();
            double max = items.get(0).main().tempMax();
            for (OwmForecastResponse.Item item : items) {
                if (item.main().tempMin() < min) min = item.main().tempMin();
                if (item.main().tempMax() > max) max = item.main().tempMax();
            }


            OwmForecastResponse.Item middle = items.get(items.size() / 2);
            OwmForecastResponse.Weather weather = middle.weather().get(0);

            // gunun adini tapiriq: wen,thurs
            String dayName = date.getDayOfWeek()
                    .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    .toUpperCase();

            // oz response doldururq
            DailyForecastResponse daily = new DailyForecastResponse();
            daily.setDayName(dayName);
            daily.setMinTemp((int) Math.round(min));
            daily.setMaxTemp((int) Math.round(max));
            daily.setIcon(weather.icon());
            daily.setDescription(capitalize(weather.description()));

            result.add(daily);
        }

        return result;
    }


    // demeli Map icersinde hazirki vaxt ve proqnozu getirib qruplasdirirq
    private Map<LocalDate, List<OwmForecastResponse.Item>> groupByDay(OwmForecastResponse owm, ZoneOffset offset) {
        Map<LocalDate, List<OwmForecastResponse.Item>> days = new LinkedHashMap<>();

        for (OwmForecastResponse.Item item : owm.list()) {
            LocalDate date = Instant.ofEpochSecond(item.dt()).atOffset(offset).toLocalDate();

            if (!days.containsKey(date)) {
                days.put(date, new ArrayList<>());
            }
            days.get(date).add(item);
        }

        return days;
    }


    // 3-cu yagis faizin yoxlanmasi

    public List<RainProbabilityResponse> getRainProbability(String city) {
        OwmForecastResponse owm = fetchForecast(city);
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(owm.city().timezone());

        List<RainProbabilityResponse> result = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            OwmForecastResponse.Item item = owm.list().get(i);

            int probability = (int) Math.round(item.pop() * 100);

            RainProbabilityResponse rain = new RainProbabilityResponse();
            rain.setTimeLabel(formatTime(item.dt(), offset, "ha"));
            rain.setProbability(probability);
            rain.setCondition(toCondition(probability));

            result.add(rain);
        }

        return result;
    }

    // yagis faizin yoxlanmasi ucun komekci metod
    private String toCondition(int probability) {
        if (probability < 30) {
            return "Sunny";
        }
        if (probability < 70) {
            return "Rainy";
        }
        return "Heavy";
    }

   }