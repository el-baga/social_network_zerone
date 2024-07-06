package com.skillbox.zerone.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.beans.ConstructorProperties;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
public class PostSearchRq {
    @Schema(example = "John")
    private String author = "";
    @Schema(example = "List of tags")
    private List<String> tags;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    @Schema(example = "Post text")
    private String text;

    @Min(value = 0, message = "Значение offset должно быть не меньше 0")
    private Integer offset = 0;

    @Schema(example = "20")
    @Min(value = 1, message = "Значение perPage должно быть не меньше 1")
    private Integer perPage = 20;

    @ConstructorProperties({"tags", "date_from", "date_to", "text"})
    PostSearchRq(String tags, Long dateFrom, Long dateTo, String text) {
        this.text = text != null && !text.isEmpty() ? text : null;
        this.tags = tags != null && !tags.isEmpty() ? List.of(tags.split(",")) : null;
        this.dateFrom = dateFrom != null && dateFrom > 0 ? Instant.ofEpochMilli(dateFrom).atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
        this.dateTo = dateTo != null && dateTo > 0? Instant.ofEpochMilli(dateTo).atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
    }
}
