package com.skillbox.zerone.mapper;

import com.skillbox.zerone.dto.response.NotificationRs;
import com.skillbox.zerone.dto.response.NotificationSettingsRs;
import com.skillbox.zerone.entity.Notification;
import com.skillbox.zerone.entity.NotificationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotifyMapper {
    NotifyMapper INSTANCE = getMapper(NotifyMapper.class);

    @Mapping(target = "info", source = "contact")
    NotificationRs toDto(Notification entity);

    @Mapping(target = "enable", source = "state", defaultValue = "false")
    @Mapping(target = "type", source = "type")
    NotificationSettingsRs toSettingDto(NotificationType type, Boolean state);
}
