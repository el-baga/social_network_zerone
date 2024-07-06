package com.skillbox.zerone.controller;

import com.skillbox.zerone.config.EndpointDescription;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.entity.Storage;
import com.skillbox.zerone.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {

    private final StorageService storageService;

    @EndpointDescription(summary = "Update profile image")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<Storage> uploadProfileImage(@RequestParam("type") String type,
                                              @RequestPart("file") MultipartFile file) {
        return storageService.uploadImage(type, file);
    }
}
