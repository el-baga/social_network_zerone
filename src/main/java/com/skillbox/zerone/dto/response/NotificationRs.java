package com.skillbox.zerone.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.zerone.entity.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationRs {
    private Long id;
    private String info;

    @JsonProperty("entity_author")
    private PersonRs sender;

    @JsonProperty("notification_type")
    private NotificationType notificationType;

    @JsonProperty("sent_time")
    private LocalDateTime sentTime;
}
