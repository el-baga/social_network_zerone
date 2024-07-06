package com.skillbox.zerone.mapper;

import com.skillbox.zerone.dto.response.DialogRs;
import com.skillbox.zerone.dto.response.MessageRs;
import com.skillbox.zerone.entity.Dialog;
import com.skillbox.zerone.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DialogMapper {

    DialogMapper INSTANCE = getMapper(DialogMapper.class);

    @Mapping(target = "lastMessage", source = "lastMessage")
    @Mapping(target = "id", source = "dialog.id", qualifiedByName = "id")
    @Mapping(target = "authorId", source = "dialog.firstPerson", qualifiedByName = "authorId")
    @Mapping(target = "recipientId", source = "dialog.secondPerson", qualifiedByName = "recipientId")
    DialogRs toDto(Dialog dialog, MessageRs lastMessage, String readStatus, Integer unreadCount);

    @Named("recipientId")
    default Integer recipientId(Person recipient) {
        return Math.toIntExact(recipient.getId());
    }

    @Named("authorId")
    default Integer authorId(Person author) {
        return Math.toIntExact(author.getId());
    }

    @Named("id")
    default Integer id(Long id) {
        return Math.toIntExact(id);
    }
}
