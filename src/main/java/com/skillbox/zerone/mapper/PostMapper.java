package com.skillbox.zerone.mapper;

import com.skillbox.zerone.dto.response.CommentRs;
import com.skillbox.zerone.dto.request.PostRq;
import com.skillbox.zerone.dto.response.PostRs;
import com.skillbox.zerone.entity.*;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mapstruct.factory.Mappers.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {
    PostMapper INSTANCE = getMapper(PostMapper.class);

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", source = "tags", qualifiedByName = "stringListToTagList")
    @Mapping(target = "isBlocked", constant = "false")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "time", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "likesList", ignore = true)
    Post toEntity(PostRq postRq);

    @Named("stringListToTagList")
    default List<Tag> stringListToTagList(List<String> strings) {
        List<Tag> tags = new ArrayList<>();
        for (String stringTag : strings) {
            Tag tag = new Tag();
            tag.setTag(stringTag);
            tags.add(tag);
        }
        return tags;
    }

    @Named("tagListToStringList")
    default List<String> tagListToStringList(List<Tag> tags) {
        return tags.stream().map(Tag::getTag).toList();
    }

    @Mapping(source = "tags", target = "tags", qualifiedByName = "tagListToStringList")
    @Mapping(source = "comments", target = "comments", qualifiedByName = "commentDTO")
    @Mapping(source = "likesList", target = "likes", qualifiedByName = "likesCount")
    @Mapping(source = "likesList", target = "myLike", qualifiedByName = "isSelfLike")
    @Mapping(source = "post", target = "type", qualifiedByName = "getPostType")
    PostRs toDto(Post post, @Context Long currentUserId);

    @Named("commentDTO")
    @Mapping(source = "comment.likesList", target = "likes", qualifiedByName = "likesCount")
    @Mapping(source = "comment.likesList", target = "myLike", qualifiedByName = "isSelfLike")
    @Mapping(source = "comment.subComments", target = "subComments", qualifiedByName = "commentDTO")
    CommentRs toCommentDto(Comment comment, @Context Long currentUserId);

    @Named("getPostType")
    default String getPostType(Post post) {
        if (Boolean.TRUE.equals(post.getIsDeleted())) {
            return PostType.DELETED.toString();
        }
        if (post.getTime().isAfter(LocalDateTime.now())) {
            return PostType.QUEUED.toString();
        }
        return PostType.POSTED.toString();
    }

    @Named("likesCount")
    default Integer likesCount(List<Like> likes) {
        return likes.size();
    }

    @Named("isSelfLike")
    default Boolean isSelfLike(List<Like> likes, @Context Long currentUserId) {
        return likes.stream().anyMatch(like -> like.getPerson().getId().equals(currentUserId));
    }
}
