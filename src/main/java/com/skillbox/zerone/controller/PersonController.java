package com.skillbox.zerone.controller;

import com.skillbox.zerone.aop.LoggableInfo;
import com.skillbox.zerone.config.EndpointDescription;
import com.skillbox.zerone.dto.request.PageSizeRq;
import com.skillbox.zerone.dto.request.PersonRq;
import com.skillbox.zerone.dto.request.PersonSearchRq;
import com.skillbox.zerone.dto.request.PostRq;
import com.skillbox.zerone.dto.response.ComplexRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.dto.response.PersonRs;
import com.skillbox.zerone.dto.response.PostRs;
import com.skillbox.zerone.service.PersonService;
import com.skillbox.zerone.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@LoggableInfo
public class PersonController {
    private final PersonService personService;
    private final PostService postService;

    @EndpointDescription(summary = "Get information about me")
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<PersonRs> aboutMe() {
        return personService.aboutMe();
    }

    @EndpointDescription(summary = "Create a new post")
    @PostMapping(value = "/{id}/wall", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<PostRs> createPost(
            @PathVariable Long id,
            @RequestBody PostRq postRq,
            @RequestParam(name = "publish_date", required = false) Long publishDate
    ) {
        return postService.createNewPost(id, postRq, publishDate);
    }

    @EndpointDescription(summary = "Get all posts by author id")
    @GetMapping(value = "/{id}/wall", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<PostRs>> getPosts(
            @PathVariable Long id,
            @ParameterObject @Valid PageSizeRq req
    ) {
        return postService.getPersonPosts(req.getOffset(), req.getPerPage(), id);
    }

    @EndpointDescription(summary = "Search users by query")
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<PersonRs>> searchPersons(@ParameterObject @Valid PersonSearchRq req) {
        return personService.searchPersons(req);
    }

    @EndpointDescription(summary = "Update information about me")
    @PutMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<PersonRs> updateMyInfo(@RequestBody @Valid PersonRq personRq) {
        return personService.updateUserInfo(personRq);
    }

    @EndpointDescription(summary = "Get user by id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<PersonRs> getPersonById(
            @PathVariable Long id
    ) {
        return personService.getPersonProfileById(id);
    }

    @EndpointDescription(summary = "Delete information about me")
    @DeleteMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<ComplexRs> deleteInfoAboutMe() {
        return personService.deleteInfoAboutMe();
    }

    @EndpointDescription(summary = "Recover information about me")
    @PostMapping(value = "/me/recover", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<ComplexRs> recoverInfoAboutMe() {
        return personService.recoverInfoAboutMe();
    }
}
