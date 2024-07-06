package com.skillbox.zerone.controller;


import com.skillbox.zerone.aop.LoggableInfo;
import com.skillbox.zerone.aop.OfflineStatus;
import com.skillbox.zerone.aop.OnlineStatusNoAuth;
import com.skillbox.zerone.config.EndpointDescription;
import com.skillbox.zerone.config.NoAuthEndpointDescription;
import com.skillbox.zerone.dto.request.LoginRq;
import com.skillbox.zerone.dto.response.CaptchaRs;
import com.skillbox.zerone.dto.response.ComplexRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.dto.response.PersonRs;
import com.skillbox.zerone.service.CaptchaService;
import com.skillbox.zerone.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@LoggableInfo
public class AuthController {

    private final PersonService personService;

    private final CaptchaService captchaService;

    @NoAuthEndpointDescription(summary = "get captcha secret code and image url")
    @GetMapping(value = "/captcha", produces = MediaType.APPLICATION_JSON_VALUE)
    public CaptchaRs getCaptcha() {
        return captchaService.sendCaptchaDto();
    }

    @OnlineStatusNoAuth
    @NoAuthEndpointDescription(summary = "login by email and password")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<PersonRs> login(@Valid @RequestBody LoginRq loginRequest) {
        return personService.login(loginRequest);
    }

    @OfflineStatus
    @EndpointDescription(summary = "logout current user")
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageRs<ComplexRs> logout() {
        return personService.logout();
    }
}
