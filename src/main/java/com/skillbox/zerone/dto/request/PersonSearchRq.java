package com.skillbox.zerone.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.beans.ConstructorProperties;

@Data
public class PersonSearchRq {
    @Schema(example = "John")
    private String firstName;
    @Schema(example = "Doe")
    private String lastName;
    @Schema(example = "Russia")
    private String country;
    @Schema(example = "Moscow")
    private String city;

    @Schema(example = "20", minimum = "0")
    @Min(value = 0, message = "Значение ageFrom должно быть не меньше 0")
    private Integer ageFrom;

    @Schema(example = "30", minimum = "0")
    @Min(value = 0, message = "Значение ageTo должно быть не меньше 0")
    private Integer ageTo;

    @Schema(example = "10", minimum = "0")
    @Min(value = 0, message = "Значение offset должно быть не меньше 0")
    private Integer offset = 0;

    @Schema(example = "20", minimum = "1")
    @Min(value = 1, message = "Значение perPage должно быть не меньше 1")
    private Integer perPage = 20;

    @ConstructorProperties({"first_name", "last_name", "age_from", "age_to"})
    PersonSearchRq(String firstName, String lastName, Integer ageFrom, Integer ageTo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ageFrom = ageFrom == null ? 0 : ageFrom;
        this.ageTo = ageTo == null ? 0 : ageTo;
    }
}
