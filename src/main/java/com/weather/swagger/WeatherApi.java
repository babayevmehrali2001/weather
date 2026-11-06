package com.weather.swagger;

import com.weather.dto.ApiResponse;
import com.weather.dto.CurrentWeatherResponse;
import com.weather.dto.DailyForecastResponse;
import com.weather.dto.RainProbabilityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Weather", description = "Hava məlumatı endpointləri")
public interface WeatherApi {

    @Operation(
            summary = "Cari hava məlumatı",
            description = "Şəhər adına görə cari temperatur, real feel, külək, təzyiq, rütubət, "
                    + "gün çıxma/batma vaxtı və hava şəraitini qaytarır.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Uğurlu cavab",
                    content = @Content(examples = @ExampleObject(value = SwaggerExamples.CURRENT_OK))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "city parametri göndərilməyib",
                    content = @Content(examples = @ExampleObject(value = SwaggerExamples.CITY_REQUIRED))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "Şəhər tapılmadı",
                    content = @Content(examples = @ExampleObject(value = SwaggerExamples.CITY_NOT_FOUND))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500", description = "Xarici API xətası",
                    content = @Content(examples = @ExampleObject(value = SwaggerExamples.SERVER_ERROR)))
    })
    ApiResponse<CurrentWeatherResponse> getCurrent(
            @Parameter(description = "Şəhər adı", required = true, example = "Baku") String city);

    @Operation(
            summary = "Günlük proqnoz",
            description = "Şəhər üçün gün-gün proqnozu qaytarır: gün adı, minimum və maksimum "
                    + "temperatur, hava ikonu və təsviri.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Uğurlu cavab",
                    content = @Content(examples = @ExampleObject(value = SwaggerExamples.FORECAST_OK))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "city parametri göndərilməyib",
                    content = @Content(examples = @ExampleObject(value = SwaggerExamples.CITY_REQUIRED))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "Şəhər tapılmadı",
                    content = @Content(examples = @ExampleObject(value = SwaggerExamples.CITY_NOT_FOUND))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500", description = "Xarici API xətası",
                    content = @Content(examples = @ExampleObject(value = SwaggerExamples.SERVER_ERROR)))
    })
    ApiResponse<List<DailyForecastResponse>> getForecast(
            @Parameter(description = "Şəhər adı", required = true, example = "Baku") String city);

    @Operation(
            summary = "Yağış ehtimalı",
            description = "Növbəti 6 zaman nöqtəsi üçün yağış ehtimalını (faizlə) və vəziyyəti "
                    + "(Sunny, Rainy, Heavy) qaytarır.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Uğurlu cavab",
                    content = @Content(examples = @ExampleObject(value = SwaggerExamples.RAIN_OK))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "city parametri göndərilməyib",
                    content = @Content(examples = @ExampleObject(value = SwaggerExamples.CITY_REQUIRED))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "Şəhər tapılmadı",
                    content = @Content(examples = @ExampleObject(value = SwaggerExamples.CITY_NOT_FOUND))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500", description = "Xarici API xətası",
                    content = @Content(examples = @ExampleObject(value = SwaggerExamples.SERVER_ERROR)))
    })
    ApiResponse<List<RainProbabilityResponse>> getRainProbability(
            @Parameter(description = "Şəhər adı", required = true, example = "Baku") String city);
}