package com.skillbox.zerone.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplexRs {

    private Integer count;

    private Integer id;

    private String message;

    @JsonProperty("message_id")
    private Integer messageId;
}
