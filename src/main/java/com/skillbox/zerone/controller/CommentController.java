package com.skillbox.zerone.controller;

import com.skillbox.zerone.aop.LoggableInfo;
import com.skillbox.zerone.config.EndpointDescription;
import com.skillbox.zerone.dto.request.CommentRq;
import com.skillbox.zerone.dto.request.PageSizeRq;
import com.skillbox.zerone.dto.response.CommentRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@LoggableInfo
public class CommentController {

    private final CommentService commentService;

    @EndpointDescription(summary = "Add new comment to a post")
    @PostMapping(value = "/{postId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<CommentRs> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRq commentRq) {
        return commentService.addComment(postId, commentRq);
    }

    @EndpointDescription(summary = "Softly delete a comment")
    @DeleteMapping(value = "/{id}/comments/{comment_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<CommentRs> softDelete(
            @PathVariable(name = "id") Long postId,
            @PathVariable(name = "comment_id") Long commentId
    ) {
        return commentService.softDelete(commentId);
    }

    @EndpointDescription(summary = "Recover a softly deleted comment")
    @PutMapping(value = "/{id}/comments/{comment_id}/recover", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<CommentRs> recover(
            @PathVariable(name = "id") Long postId,
            @PathVariable(name = "comment_id") Long commentId
    ) {
        return commentService.recover(commentId);
    }

    @EndpointDescription(summary = "Get all post's comments")
    @GetMapping(value = "/{postId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<CommentRs>> getComments(
            @PathVariable Long postId, @ParameterObject @Valid PageSizeRq req
            ) {
        return commentService.getComments(postId, req.getOffset(), req.getPerPage());
    }

    @EndpointDescription(summary = "Edit a specified comment")
    @PutMapping(value = "/{id}/comments/{comment_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<CommentRs> updateComment(
            @PathVariable(name = "id") Long postId,
            @PathVariable(name = "comment_id") Long commentId,
            @RequestBody CommentRq commentRq
    ) {
        return commentService.updateComment(commentId, commentRq);
    }
}
