package com.skillbox.zerone.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class NotificationsRq {
    @Schema(example = "false")
    private Boolean isRead = false;

    @Min(value = 0, message = "Значение offset должно быть не меньше 0")
    private Integer offset = 0;

    @Min(value = 1, message = "Значение itemPerPage должно быть не меньше 1")
    private Integer itemPerPage = 10;
}
