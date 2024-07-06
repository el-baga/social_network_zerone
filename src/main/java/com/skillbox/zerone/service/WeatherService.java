package com.skillbox.zerone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.skillbox.zerone.entity.Weather;
import com.skillbox.zerone.repository.WeatherRepository;
import com.skillbox.zerone.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;

    @Value("${weather.app.key}")
    private String appKey;

    public void getWeatherData(String city) {
        if (city == null) {
            return;
        }
        final JsonNode jsonWeather = CommonUtil.getFromWeb("https://api.openweathermap.org/data/2.5/weather?q="
                + city + "&lang=ru&units=metric&appid=" + appKey);
        if (jsonWeather == null) {
            return;
        }
        Weather weather = new Weather();
        Optional<Weather> optionalWeather = weatherRepository.findByCity(city);
        if (optionalWeather.isPresent()) {
            weather = optionalWeather.get();
        }
        weather.setOpenWeatherId(jsonWeather.get("weather").get(0).get("id").asText());
        weather.setCity(city);
        weather.setClouds(jsonWeather.get("weather").get(0).get("description").asText());
        weather.setDate(LocalDateTime.now());
        double temp = jsonWeather.get("main").get("temp").asDouble();
        weather.setTemp(Math.round(temp));
        weatherRepository.save(weather);
    }

    public void updateWeather(Set<String> cities) {
        for (String city : cities) {
            getWeatherData(city);
        }
    }

    public Weather getWeather(String city) {
        return weatherRepository.findByCity(city).orElse(null);
    }
}
