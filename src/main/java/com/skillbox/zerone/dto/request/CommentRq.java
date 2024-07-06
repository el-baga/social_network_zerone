package com.skillbox.zerone.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRq {
    @NotNull(message = "Comment text can not be null")
    @JsonProperty("comment_text")
    private String commentText;
    @JsonProperty("parent_id")
    private Long parentId;
    @JsonProperty("is_deleted")
    private Boolean isDeleted;
}
