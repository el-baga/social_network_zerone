package com.skillbox.zerone.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostRq {

    private List<String> tags;
    @NotBlank(message = "Post title can not be empty or blank")
    private String title;
    @NotBlank(message = "Post text can not be empty or blank")
    @JsonProperty("post_text")
    private String postText;
    @JsonProperty("is_deleted")
    private Boolean isDeleted;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @JsonProperty("time_delete")
    private LocalDateTime timeDelete;

}
