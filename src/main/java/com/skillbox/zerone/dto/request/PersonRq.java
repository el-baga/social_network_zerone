package com.skillbox.zerone.dto.request;

import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonRq {
    @JsonProperty("about")
    private String about;

    @JsonProperty("city")
    private String city;

    @JsonProperty("country")
    private String country;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Past(message = "Дата рождения должна быть не менее 0+")
    private LocalDateTime birthDate;

    @JsonProperty("first_name")
    @NotBlank(message = "Поле не может быть пустым")
    @Length(min = 2, max = 20)
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "Поле не может быть пустым")
    @Length(min = 2, max = 20)
    private String lastName;

    @JsonProperty("photo_id")
    private String photo;
}


