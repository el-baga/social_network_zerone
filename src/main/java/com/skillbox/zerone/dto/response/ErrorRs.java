package com.skillbox.zerone.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@Builder
public class ErrorRs {
    private String error;
    private Timestamp timestamp;
    private String  errorDescription;
}
