package com.skillbox.zerone.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class NotificationsSetRq {
    @Schema(example = "false")
    Boolean all = false;

    @Min(value = 0, message = "Значение id должно быть не меньше 0")
    private Long id = 0L;
}
