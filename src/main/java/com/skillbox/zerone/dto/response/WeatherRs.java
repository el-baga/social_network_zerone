package com.skillbox.zerone.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WeatherRs {

    private String city;
    private String clouds;
    private LocalDateTime date;
    private String temp;
}
