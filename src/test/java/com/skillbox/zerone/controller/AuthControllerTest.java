package com.skillbox.zerone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.zerone.config.TestConfig;
import com.skillbox.zerone.dto.request.LoginRq;
import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.repository.CaptchaRepository;
import com.skillbox.zerone.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@Sql(value = "/sql/personController-beforeTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/sql/personController-afterTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CaptchaRepository captchaRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Get captcha secret code and image url with status 200")
    @WithAnonymousUser
    void testGetCaptcha_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/auth/captcha");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        boolean isCaptchaExists = captchaRepository.existsById(1L);
        Assertions.assertTrue(isCaptchaExists);
    }

    @Test
    @DisplayName("Login by email and password with status 200")
    @WithAnonymousUser
    void testLogin_withStatus200() throws Exception {
        String email = "baga@mail.com";
        String password = "javaSE2024";
        LoginRq loginRq = LoginRq.builder()
                .chatId(null)
                .email(email)
                .password(password)
                .build();

        Person person = personService.getByEmail(email);
        String encodedPassword = passwordEncoder.encode(password);
        person.setPassword(encodedPassword);
        personService.updatePerson(person);

        this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRq)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Login by email and password with status 400 (Invalid email)")
    @WithAnonymousUser
    void testLoginInvalidEmail_withStatus400() throws Exception {
        String email = "testInvalid@email.ru";
        String password = "javaSE2024";
        LoginRq loginRq = LoginRq.builder()
                .chatId(null)
                .email(email)
                .password(password)
                .build();

        this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login by email and password with status 400 (Person is blocked)")
    @WithAnonymousUser
    void testLoginPersonIsBlocked_withStatus400() throws Exception {
        String email2 = "test-block@mail.ru";
        String password2 = "irina2023";
        LoginRq loginRq = LoginRq.builder()
                .chatId(null)
                .email(email2)
                .password(password2)
                .build();

        this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login by email and password with status 400 (Invalid password)")
    @WithAnonymousUser
    void testLoginInvalidPassword_withStatus400() throws Exception {
        String email3 = "baga@mail.com";
        String password3 = "javaSE2023";
        LoginRq loginRq = LoginRq.builder()
                .chatId(null)
                .email(email3)
                .password(password3)
                .build();

        Person person = personService.getByEmail(email3);
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        personService.updatePerson(person);

        this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Logout current user with status 200")
    @WithMockUser("1")
    void testLogout_withStatus200() throws Exception {
        this.mockMvc.perform(post("/api/v1/auth/logout"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Logout current user with status 401")
    @WithAnonymousUser
    void testAboutMe_withStatus401() throws Exception {
        this.mockMvc.perform(post("/api/v1/auth/logout"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
