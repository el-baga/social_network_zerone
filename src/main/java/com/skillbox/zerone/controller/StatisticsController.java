package com.skillbox.zerone.controller;

import com.skillbox.zerone.config.EndpointDescription;
import com.skillbox.zerone.dto.response.RegionStatisticsRs;
import com.skillbox.zerone.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @EndpointDescription(summary = "Get user count")
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getUserCount() {
        return statisticsService.getUserCount();
    }

    @GetMapping(value = "/user/blocked", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getBlockedUserCount() {
        return statisticsService.getBlockedUserCount();
    }

    @GetMapping(value = "user/deleted", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getSoftDeletedUserCount(){
        return statisticsService.getSoftDeletedUserCount();
    }

    @EndpointDescription(summary = "Get user count by country name")
    @GetMapping(value = "/user/country", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getUserCountByCounty(@RequestParam String country) {
        return statisticsService.getUserCountByCountry(country);
    }

    @EndpointDescription(summary = "Get user count by city name")
    @GetMapping(value = "/user/city", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getUserCountByCity(@RequestParam String city) {
        return statisticsService.getUserCountByCity(city);
    }

    @EndpointDescription(summary = "Get tag count")
    @GetMapping(value = "/tag", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getTagCount() {
        return statisticsService.getTagCount();
    }

    @EndpointDescription(summary = "Get tag count by post id")
    @GetMapping(value = "/tag/post", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getTagCountByPostId(@RequestParam(name = "post_id") Long postId) {
        return statisticsService.getTagCountByPostId(postId);
    }

    @EndpointDescription(summary = "Get post count")
    @GetMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getPostCount() {
        return statisticsService.getPostCount();
    }

    @EndpointDescription(summary = "Get post count by user id")
    @GetMapping(value = "/post/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getPostCountByUserId(@RequestParam(name = "user_id") Long userId) {
        return statisticsService.getPostCountByUserId(userId);
    }

    @EndpointDescription(summary = "Get message count")
    @GetMapping(value = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getMessageCount() {
        return statisticsService.getMessageCount();
    }

    @EndpointDescription(summary = "Get message count by dialog id")
    @GetMapping(value = "/message/dialog", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getMessageCountByDialogId(@RequestParam(name = "dialog_id") Long dialogId) {
        return statisticsService.getMessageCountByDialogId(dialogId);
    }

    @EndpointDescription(summary = "Get the number of messages by id's of two persons." +
            " This method return map where key is description who author, and who recipient." +
            " And value is number of message")
    @GetMapping(value = "/message/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> getMessageByIdOfTwoPersons(@RequestParam(name = "first_user_id") Long firstUserId,
                                                        @RequestParam(name = "second_user_id") Long secondUserId) {
        return statisticsService.getMessageByIdOfTwoPersons(firstUserId, secondUserId);
    }

    @EndpointDescription(summary = "Get like count")
    @GetMapping(value = "/like", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getLikeCount() {
        return statisticsService.getLikeCount();
    }

    @EndpointDescription(summary = "Get like count by post or comment id")
    @GetMapping(value = "/like/entity", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getLikeCountByEntityId(@RequestParam(name = "comment_id") Long commentId) {
        return statisticsService.getLikeCountByEntityId(commentId);
    }

    @EndpointDescription(summary = "Get dialog count")
    @GetMapping(value = "/dialog", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getDialogCount() {
        return statisticsService.getDialogCount();
    }

    @EndpointDescription(summary = "Get dialog count by user id")
    @GetMapping(value = "/dialog/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getDialogCountByUserId(@RequestParam(name = "user_id") Long userId) {
        return statisticsService.getDialogCountByUserId(userId);
    }

    @EndpointDescription(summary = "Get country count")
    @GetMapping(value = "/country", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getCountryCount() {
        return statisticsService.getCountryCount();
    }

    @EndpointDescription(summary = "Get user count in each country")
    @GetMapping(value = "/country/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RegionStatisticsRs> getUsersCountInEachCountry() {
        return statisticsService.getUsersCountInEachCountry();
    }

    @EndpointDescription(summary = "Get comment count by post id")
    @GetMapping(value = "/comment/post", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getCommentCountByPostId(@RequestParam(name = "post_id") Long postId) {
        return statisticsService.getCommentCountByPostId(postId);
    }

    @EndpointDescription(summary = "Get city count")
    @GetMapping(value = "/city", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getCityCount() {
        return statisticsService.getCityCount();
    }

    @EndpointDescription(summary = "Get user count in each city")
    @GetMapping(value = "/city/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RegionStatisticsRs> getUsersCountInEachCity() {
        return statisticsService.getUsersCountInEachCity();
    }
}
