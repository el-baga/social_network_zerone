package com.skillbox.zerone.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaptchaRs {
    @Schema(example = "123456")
    private String code;
    @Schema(example = "/some/path")
    private String image;
}
