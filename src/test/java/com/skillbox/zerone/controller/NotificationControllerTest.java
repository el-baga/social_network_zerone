package com.skillbox.zerone.controller;

import com.skillbox.zerone.config.TestConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/notificationController-beforeTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/notificationController-afterTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class NotificationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("Получение списка оповещений")
    @WithMockUser("3")
    void testGetNotifications_result200() throws Exception {
        final var expectedValue = 3;
        this.mockMvc
            .perform(get("/api/v1/notifications"))
            .andDo(print())
            .andExpectAll(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(expectedValue))
            .andExpect(jsonPath("$.total").value(expectedValue));
    }

    @Test
    @Order(2)
    @DisplayName("Пометка оповещения о прочтении")
    @WithMockUser("3")
    void testSetNotificationRead_custom_result200() throws Exception {
        final var all = false;
        final var notifyId = 1L;
        final var expectedValue = "success";
        this.mockMvc
            .perform(put(String.format("/api/v1/notifications?all=%s&id=%d", all, notifyId)))
            .andDo(print())
            .andExpectAll(status().isOk())
            .andExpect(jsonPath("$.data").value(expectedValue));

        final var expectedTotal = 2;
        this.mockMvc
            .perform(get("/api/v1/notifications"))
            .andDo(print())
            .andExpectAll(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(expectedTotal))
            .andExpect(jsonPath("$.total").value(expectedTotal));
    }

    @Test
    @Order(3)
    @DisplayName("Пометка всех оповещений о прочтении")
    @WithMockUser("3")
    void testSetNotificationRead_all_result200() throws Exception {
        final var all = true;
        final var notifyId = 0L;
        final var expectedValue = "success";
        this.mockMvc
            .perform(put(String.format("/api/v1/notifications?all=%s&id=%d", all, notifyId)))
            .andDo(print())
            .andExpectAll(status().isOk())
            .andExpect(jsonPath("$.data").value(expectedValue));

        final var expectedTotal = 0;
        this.mockMvc
            .perform(get("/api/v1/notifications"))
            .andDo(print())
            .andExpectAll(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(expectedTotal))
            .andExpect(jsonPath("$.total").value(expectedTotal));
    }
}
