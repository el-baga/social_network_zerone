package com.skillbox.zerone.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRq {

    @JsonProperty("email")
    private String email;

    @JsonProperty("secret")
    private String secret;
}
