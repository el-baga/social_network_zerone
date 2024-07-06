package com.skillbox.zerone.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRs {
    @Schema(example = "Comment's author")
    private PersonRs author;
    @Schema(example = "123456")
    private Long id;
    @Schema(example = "100")
    private Integer likes;
    @Schema(example = "2024-01-01T12:00:00.000000")
    private LocalDateTime time;

    @JsonProperty("comment_text")
    private String commentText;

    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    @JsonProperty("is_deleted")
    private Boolean isDeleted;

    @JsonProperty("my_like")
    private Boolean myLike;

    @JsonProperty("parent_id")
    private Long parentId;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("sub_comments")
    private List<CommentRs> subComments;
}
