package com.skillbox.zerone.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
@Builder
public class LikeRs {
    private Long likes;
    private List<Long> users;
}
