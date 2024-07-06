package com.skillbox.zerone.controller;

import com.skillbox.zerone.aop.LoggableInfo;
import com.skillbox.zerone.config.EndpointDescription;
import com.skillbox.zerone.dto.request.LikeRq;
import com.skillbox.zerone.dto.response.LikeRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
@LoggableInfo
public class LikeController {

    private final LikeService likeService;

    @EndpointDescription(summary = "Add a like to a post or a comment")
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<LikeRs> addLike(
            @RequestBody LikeRq likeRq) {
        return likeService.addLike(likeRq);
    }

    @EndpointDescription(summary = "Remove a like")
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<LikeRs> removeLike(
            @RequestParam(name = "item_id") Long itemId,
            @RequestBody LikeRq likeRq) {
        return likeService.removeLike(likeRq, itemId);
    }

    @EndpointDescription(summary = "Get likes list of a post or a comment")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<LikeRs> getLikes(
            @RequestParam(name = "item_id") Long itemId) {
        return likeService.getLikeRsPageRs(itemId);
    }
}
