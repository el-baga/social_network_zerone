package com.skillbox.zerone.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DialogRs {

    private Integer id;

    @JsonProperty("author_id")
    private Integer authorId;

    @JsonProperty("last_message")
    private MessageRs lastMessage;

    @JsonProperty("read_status")
    private String readStatus;

    @JsonProperty("recipient_id")
    private Integer recipientId;

    @JsonProperty("unread_count")
    private Integer unreadCount;
}

