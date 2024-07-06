package com.skillbox.zerone.mapper;

import com.skillbox.zerone.dto.request.PersonRq;
import com.skillbox.zerone.dto.response.PersonRs;
import com.skillbox.zerone.dto.request.RegisterRq;
import com.skillbox.zerone.entity.Person;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import static org.mapstruct.factory.Mappers.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonMapper {
    PersonMapper INSTANCE = getMapper(PersonMapper.class);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @BeanMapping(ignoreByDefault = true)
    Person toEntity(RegisterRq dto);

    @Mapping(source = "changePasswordToken", target = "token")
    @Mapping(source = "onlineStatus", target = "online")
    PersonRs toDto(Person entity);

    @Mapping(target = "token", ignore = true)
    @Mapping(target = "email", source = "person.email")
    @Mapping(target = "id", source = "person.id")
    @Mapping(target = "online", source = "person.onlineStatus")
    @Mapping(target = "photo", source = "person.photo")
    @Mapping(target = "firstName", source = "person.firstName")
    @Mapping(target = "friendStatus", source = "friendStatus")
    @Mapping(target = "isBlocked", source = "person.isBlocked")
    @Mapping(target = "isBlockedByCurrentUser", source = "isBlockedByCurrentUser")
    @Mapping(target = "lastName", source = "person.lastName")
    @Mapping(target = "lastOnlineTime", source = "person.lastOnlineTime")
    @Mapping(target = "messagePermission", source = "person.messagePermissions")
    @Mapping(target = "userDeleted", source = "person.isDeleted")
    PersonRs personToPersonDTO(Person person, String friendStatus, Boolean isBlockedByCurrentUser);

    @Mapping(target = "firstName", source = "dto.firstName")
    @Mapping(target = "lastName", source = "dto.lastName")
    @Mapping(target = "about", source = "dto.about")
    @Mapping(target = "city", source = "dto.city")
    @Mapping(target = "country", source = "dto.country")
    @Mapping(target = "phone", source = "dto.phone")
    @Mapping(target = "birthDate", source = "dto.birthDate")
    @Mapping(target = "photo", source = "person.photo")
    @Mapping(target = "id", source = "person.id")
    @Mapping(target = "email", source = "person.email")
    @Mapping(target = "messagePermissions", source = "person.messagePermissions")
    @Mapping(target = "regDate", source = "person.regDate")
    @Mapping(target = "changePasswordToken", source = "person.changePasswordToken")
    @Mapping(target = "configurationCode", source = "person.configurationCode")
    @Mapping(target = "isDeleted", source = "person.isDeleted")
    @Mapping(target = "isApproved", source = "person.isApproved")
    @Mapping(target = "isBlocked", source = "person.isBlocked")
    @Mapping(target = "deletedTime", source = "person.deletedTime")
    @Mapping(target = "lastOnlineTime", source = "person.lastOnlineTime")
    @Mapping(target = "notificationsSessionId", source = "person.notificationsSessionId")
    @Mapping(target = "onlineStatus", source = "person.onlineStatus")
    @Mapping(target = "password", source = "person.password")
    @Mapping(target = "telegramId", source = "person.telegramId")
    @Mapping(target = "personSettings", source = "person.personSettings")
    @Mapping(target = "emailUUID", source = "person.emailUUID")
    Person toEntity(PersonRq dto, Person person);
}
