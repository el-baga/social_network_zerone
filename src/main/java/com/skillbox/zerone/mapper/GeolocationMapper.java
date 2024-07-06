package com.skillbox.zerone.mapper;

import com.skillbox.zerone.dto.response.GeolocationRs;
import com.skillbox.zerone.entity.City;
import com.skillbox.zerone.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GeolocationMapper {
    GeolocationMapper INSTANCE = getMapper(GeolocationMapper.class);

    @Mapping(target = "title", source = "name")
    GeolocationRs toDto(City city);

    @Mapping(target = "title", source = "name")
    GeolocationRs toDto(Country country);

    GeolocationRs toDto(com.vk.api.sdk.objects.database.City city);
}
