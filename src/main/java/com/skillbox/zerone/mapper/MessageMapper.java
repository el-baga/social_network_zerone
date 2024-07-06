package com.skillbox.zerone.mapper;

import com.skillbox.zerone.dto.request.MessageWs;
import com.skillbox.zerone.dto.response.MessageRs;
import com.skillbox.zerone.dto.response.PersonRs;
import com.skillbox.zerone.entity.Dialog;
import com.skillbox.zerone.entity.Message;
import com.skillbox.zerone.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {
    MessageMapper INSTANCE = getMapper(MessageMapper.class);

    @Mapping(target = "recipientId", source = "message.recipient", qualifiedByName = "recipientId")
    @Mapping(target = "authorId", source = "message.author", qualifiedByName = "authorId")
    @Mapping(target = "recipient", source = "recipient")
    @Mapping(target = "id", source = "message.id")
    MessageRs toDto(Message message, Boolean isSentByMe, PersonRs recipient);

    @Named("recipientId")
    default Integer recipientId(Person recipient) {
        return Math.toIntExact(recipient.getId());
    }

    @Named("authorId")
    default Integer authorId(Person author) {
        return Math.toIntExact(author.getId());
    }

    @Mapping(target = "readStatus", constant = "UNREAD")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "time", expression = "java(java.time.LocalDateTime.now())")
    Message toEntity(MessageWs messageWs, Dialog dialog, Person author, Person recipient);
}
