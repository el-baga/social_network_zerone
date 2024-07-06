package com.skillbox.zerone.controller;

import com.skillbox.zerone.aop.LoggableInfo;
import com.skillbox.zerone.config.EndpointDescription;
import com.skillbox.zerone.dto.request.NotificationsRq;
import com.skillbox.zerone.dto.request.NotificationsSetRq;
import com.skillbox.zerone.dto.response.NotificationRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@LoggableInfo
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @EndpointDescription(summary = "Get notification list")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<NotificationRs>> getNotifications(@ParameterObject @Valid NotificationsRq req) {
        return notificationService.getNotifications(req);
    }

    @EndpointDescription(summary = "Set notification as read")
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<String> setNotificationRead(@ParameterObject @Valid NotificationsSetRq req) {
        return notificationService.setNotificationRead(req);
    }
}
