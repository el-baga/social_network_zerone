package com.skillbox.zerone.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BadRequestRs {
    @Schema(example = "BadRequestException")
    private String error;

    @Schema(example = "123456789")
    private Long timestamp;

    @Schema(example = "Введены некорректные данные")
    @JsonProperty("error_description")
    private String errorDescription;
}
