package com.skillbox.zerone.mapper;

import com.skillbox.zerone.dto.request.StorageRq;
import com.skillbox.zerone.entity.Storage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StorageMapper {

    @Mapping(target = "id", ignore = true)
    Storage toEntity(StorageRq dto);
}
