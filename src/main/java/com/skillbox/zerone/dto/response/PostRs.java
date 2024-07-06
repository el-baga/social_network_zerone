package com.skillbox.zerone.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.zerone.dto.response.CommentRs;
import com.skillbox.zerone.dto.response.PersonRs;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostRs {
    private PersonRs author;
    private List<CommentRs> comments;
    private Long id;
    private Integer likes;
    private List<String> tags;
    private String title;
    private String type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime time;

    @JsonProperty("my_like")
    private Boolean myLike;

    @JsonProperty("post_text")
    private String postText;

    @JsonProperty("blocked")
    private Boolean isBlocked;
}
