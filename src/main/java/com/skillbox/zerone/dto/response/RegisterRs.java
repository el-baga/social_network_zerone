package com.skillbox.zerone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRs {

    private ComplexRs data;

    private String email;
    @Builder.Default
    private Long timestamp = System.currentTimeMillis();

}
