package com.skillbox.zerone.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRq {

    @NotBlank(message = "Поле не может быть пустым")
    private String email;

    @NotBlank(message = "Поле не может быть пустым")
    private String password;

    @Hidden
    private Long chatId;
}