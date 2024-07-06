package com.skillbox.zerone.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRs {
    private Integer id;

    private Boolean isSentByMe;

    private PersonRs recipient;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime time;

    @JsonProperty("author_id")
    private Integer authorId;

    @JsonProperty("message_text")
    private String messageText;

    @JsonProperty("read_status")
    private String readStatus;

    @JsonProperty("recipient_id")
    private Integer recipientId;
}
