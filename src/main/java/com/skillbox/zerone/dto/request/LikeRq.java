package com.skillbox.zerone.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class LikeRq {
    @JsonProperty("item_id")
    private Long itemId;
    @JsonProperty("post_id")
    private Long postId;
    private String type;
}
