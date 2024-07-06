package com.skillbox.zerone.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRecoveryRq {

    @JsonProperty("email")
    private String email;
}
