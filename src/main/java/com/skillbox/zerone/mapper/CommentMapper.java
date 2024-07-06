package com.skillbox.zerone.mapper;

import com.skillbox.zerone.dto.request.CommentRq;
import com.skillbox.zerone.entity.Comment;
import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    CommentMapper INSTANCE = getMapper(CommentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "time", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isDeleted", source = "commentRq.isDeleted", defaultValue = "false")
    @Mapping(target = "timeDelete", ignore = true)
    @Mapping(target = "author", source = "author")
    @Mapping(target = "post", source = "post")
    Comment toEntity(CommentRq commentRq, Long postId, Person author, Post post, List<Comment> subComments);
}
