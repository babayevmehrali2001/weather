package com.weather.swagger;


public final class SwaggerExamples {

    private SwaggerExamples() {
    }


    public static final String CURRENT_OK = """
            {
              "success": true,
              "data": {
                "temp": 18.03,
                "feels_like": 18.34,
                "wind_speed": 4.62,
                "wind_direction": "S",
                "pressure": 1014,
                "humidity": 94,
                "sunrise": "07:13",
                "sunset": "17:34",
                "icon": "03d",
                "description": "Scattered clouds",
                "city_name": "Baku",
                "datetime": "2026-11-06 10:00"
              },
              "message": "OK"
            }""";

    public static final String FORECAST_OK = """
            {
              "success": true,
              "data": [
                { "day_name": "FRI", "min_temp": 15, "max_temp": 20, "icon": "03d", "description": "Scattered clouds" },
                { "day_name": "SAT", "min_temp": 14, "max_temp": 19, "icon": "10d", "description": "Light rain" },
                { "day_name": "SUN", "min_temp": 13, "max_temp": 18, "icon": "04d", "description": "Overcast clouds" },
                { "day_name": "MON", "min_temp": 12, "max_temp": 17, "icon": "01d", "description": "Clear sky" },
                { "day_name": "TUE", "min_temp": 13, "max_temp": 19, "icon": "02d", "description": "Few clouds" }
              ],
              "message": "OK"
            }""";

    public static final String RAIN_OK = """
            {
              "success": true,
              "data": [
                { "time_label": "1PM",  "probability": 9,  "condition": "Sunny" },
                { "time_label": "4PM",  "probability": 25, "condition": "Sunny" },
                { "time_label": "7PM",  "probability": 45, "condition": "Rainy" },
                { "time_label": "10PM", "probability": 62, "condition": "Rainy" },
                { "time_label": "1AM",  "probability": 78, "condition": "Heavy" },
                { "time_label": "4AM",  "probability": 40, "condition": "Rainy" }
              ],
              "message": "OK"
            }""";


    public static final String CITY_REQUIRED = """
            { "success": false, "data": null, "message": "City parameter is required" }""";

    public static final String CITY_NOT_FOUND = """
            { "success": false, "data": null, "message": "City 'Bakuuu' not found" }""";

    public static final String SERVER_ERROR = """
            { "success": false, "data": null, "message": "Weather service error" }""";
}