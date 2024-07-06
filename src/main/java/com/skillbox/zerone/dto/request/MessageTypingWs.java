package com.skillbox.zerone.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageTypingWs {
    private Boolean typing;

    @JsonProperty("user_id")
    private Long userId;
}
