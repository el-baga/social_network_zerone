package com.skillbox.zerone.controller;

import com.skillbox.zerone.aop.LoggableInfo;
import com.skillbox.zerone.config.EndpointDescription;
import com.skillbox.zerone.config.NoAuthEndpointDescription;
import com.skillbox.zerone.dto.request.*;
import com.skillbox.zerone.dto.response.NotificationSettingsRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.dto.response.RegisterRs;
import com.skillbox.zerone.listener.KafkaMessage;
import com.skillbox.zerone.service.NotificationService;
import com.skillbox.zerone.service.PersonService;
import com.skillbox.zerone.util.CommonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@LoggableInfo
public class AccountController {
    @Value("${app.kafka.kafkaMessageTopicEmail}")
    private String topicNameEmail;

    @Value("${app.kafka.kafkaMessageTopicPassword}")
    private String topicNamePassword;

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;
    private final PersonService personService;
    private final NotificationService notificationService;

    @NoAuthEndpointDescription(summary = "User registration")
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegisterRs register(@Valid @RequestBody RegisterRq dto) {
        return personService.create(dto);
    }

    @EndpointDescription(summary = "get user's notifications settings")
    @GetMapping(value = "/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<List<NotificationSettingsRs>> getNotificationsSettings() {
        return notificationService.getSettings();
    }

    @EndpointDescription(summary = "edit user's notifications settings")
    @PutMapping(value = "/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<String> setNotificationSetting(@Valid @RequestBody NotificationSettingRq req) {
        return notificationService.setSetting(req);
    }

    @EndpointDescription(summary = "Set email")
    @PutMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegisterRs setPersonEmail(@RequestBody EmailRq emailRq) {
        return personService.setPersonEmail(emailRq);
    }

    @EndpointDescription(summary = "Change email")
    @PutMapping(value = "/email/recovery")
    public Long changePersonEmail(@RequestBody String personEmail) {
        kafkaTemplate.send(topicNameEmail, KafkaMessage.builder().message(personEmail)
                .userId(CommonUtil.getCurrentUserId()).build());
        return System.currentTimeMillis();
    }
    
    @PutMapping("/password/set")
    public RegisterRs setPersonPassword(@RequestBody PasswordSetRq passwordSetRq) {
        return personService.setPersonPassword(passwordSetRq);
    }

    @PutMapping("/password/reset")
    public RegisterRs resetPersonPassword(@RequestBody PasswordResetRq passwordResetRq) {
        return personService.resetPersonPassword(passwordResetRq);
    }

    @PutMapping("/password/recovery")
    public Long recoverPersonPassword(@RequestBody PasswordRecoveryRq passwordRecoveryRq) {
        kafkaTemplate.send(topicNamePassword, KafkaMessage.builder().message(passwordRecoveryRq.getEmail())
                .userId(personService.getByEmail(passwordRecoveryRq.getEmail()).getId()).build());
        return System.currentTimeMillis();
    }
}
