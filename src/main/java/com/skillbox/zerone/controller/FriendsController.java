package com.skillbox.zerone.controller;

import com.skillbox.zerone.aop.LoggableInfo;
import com.skillbox.zerone.config.EndpointDescription;
import com.skillbox.zerone.dto.request.PageSizeRq;
import com.skillbox.zerone.dto.response.ComplexRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.dto.response.PersonRs;
import com.skillbox.zerone.service.FriendsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
@LoggableInfo
public class FriendsController {
    private final FriendsService friendsService;

    @EndpointDescription(summary = "Send a friendship request")
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<ComplexRs> sendFriendshipRequest(@PathVariable Integer id) {
        return friendsService.sendFriendshipRequest(id);
    }

    @EndpointDescription(summary = "Remove a friend by id")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<ComplexRs> deleteFriendById(@PathVariable("id") Integer id) {
        return friendsService.deleteFriendById(id);
    }

    @EndpointDescription(summary = "Add a friend by id")
    @PostMapping(value = "/request/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<ComplexRs> addFriendById(@PathVariable Integer id) {
        return friendsService.addFriendById(id);
    }

    @EndpointDescription(summary = "Decline a friendship request by id")
    @DeleteMapping(value = "/request/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<ComplexRs> declineFriendshipRequestById(@PathVariable Integer id) {
        return friendsService.declineFriendshipRequestById(id);
    }

    @EndpointDescription(summary = "Block/unblock a friend by id")
    @PostMapping(value = "/block_unblock/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<ComplexRs> blockUnblockFriendById(@PathVariable Integer id) {
        return friendsService.blockUnblockFriendById(id);
    }

    @EndpointDescription(summary = "Get friends list")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<PersonRs>> getFriends(@ParameterObject @Valid PageSizeRq req) {
        return friendsService.getFriendsResponse(req.getOffset(), req.getPerPage());
    }

    @EndpointDescription(summary = "Get incoming friendship requests")
    @GetMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<PersonRs>> getFriendsRequest(@ParameterObject @Valid PageSizeRq req) {
        return friendsService.getFriendsRequestResponse(req.getOffset(), req.getPerPage());
    }

    @EndpointDescription(summary = "Get friendships recommendations")
    @GetMapping(value = "/recommendations", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<PersonRs>> getFriendsRecommendation() {
        return friendsService.getFriendsRecommendation();
    }

    @EndpointDescription(summary = "Get outgoing friendship requests")
    @GetMapping(value = "/outgoing_requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<PersonRs>> getFriendsOutgoingRequest(@ParameterObject @Valid PageSizeRq req) {
        return friendsService.getFriendsOutgoingRequestResponse(req.getOffset(), req.getPerPage());
    }
}
