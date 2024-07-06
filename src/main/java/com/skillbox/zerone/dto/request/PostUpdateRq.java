package com.skillbox.zerone.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class PostUpdateRq {
    @JsonProperty("post_text")
    private String postText;
    private List<String> tags;
    private String title;
}
