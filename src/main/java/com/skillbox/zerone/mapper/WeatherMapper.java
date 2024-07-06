package com.skillbox.zerone.mapper;

import com.skillbox.zerone.dto.response.WeatherRs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.skillbox.zerone.entity.Weather;
import org.mapstruct.MappingConstants;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WeatherMapper {

    WeatherMapper INSTANCE = getMapper(WeatherMapper.class);
    
    @Mapping(target = "city", source = "city")
    @Mapping(target = "clouds", source = "clouds")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "temp", source = "temp")
    WeatherRs weatherToWeatherDTO(Weather weather);
    
}
