package com.skillbox.zerone.controller;

import com.skillbox.zerone.aop.LoggableInfo;
import com.skillbox.zerone.config.EndpointDescription;
import com.skillbox.zerone.dto.request.PageSizeRq;
import com.skillbox.zerone.dto.request.PostSearchRq;
import com.skillbox.zerone.dto.request.PostUpdateRq;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.dto.response.PostRs;
import com.skillbox.zerone.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@LoggableInfo
public class PostController {
    private final PostService postService;

    @EndpointDescription(summary = "Get posts of all users")
    @GetMapping(value = "/feeds", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<PostRs>> getFeeds(@ParameterObject @Valid PageSizeRq req) {
        return postService.getFeeds(req.getOffset(), req.getPerPage());
    }

    @EndpointDescription(summary = "Edit post")
    @PutMapping(value = "/post/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<PostRs> updatePost(
            @PathVariable Long id,
            @RequestBody PostUpdateRq postUpdateRq
    ) {
        return postService.updatePost(id, postUpdateRq);
    }

    @EndpointDescription(summary = "Get post by id")
    @GetMapping(value = "/post/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<PostRs> getPost(
            @PathVariable Long id
    ) {
        return postService.getPost(id);
    }

    @EndpointDescription(summary = "Softly delete post by id")
    @DeleteMapping(value = "/post/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<PostRs> softDelete(@PathVariable Long id) {
        return postService.softDeleteOrRecover(id, true);
    }

    @EndpointDescription(summary = "Recover post by id")
    @PutMapping(value = "/post/{id}/recover", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<PostRs> recoverPost(@PathVariable Long id) {
        return postService.softDeleteOrRecover(id, false);
    }

    @EndpointDescription(summary = "Search posts by query")
    @GetMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<PostRs>> searchPosts(@ParameterObject @Valid PostSearchRq req) {
        return postService.searchPosts(req);
    }

}
