package com.skillbox.zerone.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.skillbox.zerone.entity.NotificationType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationSettingRq {
    @NotNull(message = "Поле enable должно быть true/false")
    private Boolean enable;

    @NotNull(message = "Поле notification_type не может быть пустым")
    @JsonAlias("notification_type")
    private NotificationType notificationType;
}
