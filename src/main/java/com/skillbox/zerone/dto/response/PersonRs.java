package com.skillbox.zerone.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema
public class PersonRs {

    private String about;
    private String city;
    private String country;
    private CurrencyRs currency;
    private String email;
    private Long id;
    private Boolean online;
    private String phone;
    private String photo;
    private String token;
    private WeatherRs weather;

    @JsonProperty("birth_date")
    private LocalDateTime birthDate;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("friend_status")
    private String friendStatus;

    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    @JsonProperty("is_blocked_by_current_user")
    private Boolean isBlockedByCurrentUser;

    @JsonProperty("last_online_time")
    private LocalDateTime lastOnlineTime;

    @JsonProperty("message_permission")
    private String messagePermission;

    @JsonProperty("reg_date")
    private LocalDate regDate;

    @JsonProperty("user_deleted")
    private Boolean userDeleted;

}
