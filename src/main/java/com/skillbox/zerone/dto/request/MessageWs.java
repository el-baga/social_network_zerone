package com.skillbox.zerone.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MessageWs {
    private Long id;

    @JsonProperty("dialog_id")
    private Long dialogId;

    @JsonProperty("author_id")
    private Long authorId;

    @JsonProperty("recipient_id")
    private Long recipientId;

    @JsonProperty("message_text")
    private String messageText;

    @JsonProperty("read_status")
    private String readStatus;

    @Schema(description = "Current time", example = "12432857239")
    private Long timestamp = System.currentTimeMillis();

    private String token;
}
