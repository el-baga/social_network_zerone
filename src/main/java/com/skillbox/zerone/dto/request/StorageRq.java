package com.skillbox.zerone.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StorageRq {

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private String fileName;

    private Long fileSize;

    private String fileType;

    private Long ownerId;
}
