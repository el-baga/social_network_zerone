package com.skillbox.zerone.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class DialogUserShortListRq {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_ids")
    private List<Integer> userIds;
}
