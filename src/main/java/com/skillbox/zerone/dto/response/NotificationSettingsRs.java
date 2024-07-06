package com.skillbox.zerone.dto.response;

import com.skillbox.zerone.entity.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationSettingsRs {
    private String description;
    private NotificationType type;
    private Boolean enable;
}
