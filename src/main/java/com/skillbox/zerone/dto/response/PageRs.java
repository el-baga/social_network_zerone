package com.skillbox.zerone.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema
public class PageRs<T> {
    @Builder.Default
    @Schema(description = "Current time", example = "12432857239")
    private Long timestamp = System.currentTimeMillis();
    @Schema(example = "Collection of objects or just object any type")
    private T data;
    @Schema(example = "20")
    private Integer itemPerPage;
    @Schema(example = "0")
    private Integer offset;
    @Schema(example = "20")
    private Integer perPage;
    @Schema(example = "500")
    private Long total;
}
