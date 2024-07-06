package com.skillbox.zerone.controller;

import com.skillbox.zerone.config.TestConfig;
import org.junit.jupiter.api.Test;

import com.skillbox.zerone.dto.request.DialogUserShortListRq;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/dialogControllerInsert.sql")
@Sql(scripts = "/sql/dialogControllerClean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@AutoConfigureMockMvc
@WithMockUser(username = "1", authorities = {"USER"})

class DialogControllerTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    MockMvc mockMvc;

    @DisplayName("Read messages in dialog test")
    @Test
    void changeMessagesStatus() throws Exception {
        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/dialogs/1/read"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.message")
                        .value("Message status change to READ in dialog with id 1"));
    }

    @WithAnonymousUser
    @DisplayName("Read messages in dialog test with status 401")
    @Test
    void changeMessagesStatus_withStatus401() throws Exception {
        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/dialogs/1/read"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @DisplayName("Read messages in dialog test with status 400")
    @Test
    void changeMessagesStatus_withStatus400() throws Exception {
        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/dialogs/100/read"))
                .andDo(print())
                .andExpectAll(status().isBadRequest());
    }

    @DisplayName("Get dialogs by user test")
    @Test
    void getDialogsByUser() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/dialogs"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].author_id").value(1))
                .andExpect(jsonPath("$.data[0].last_message.id").value(1))
                .andExpect(jsonPath("$.data[0].last_message.isSentByMe").value(true))
                .andExpect(jsonPath("$.data[0].last_message.recipient.id").value(2))
                .andExpect(jsonPath("$.data[0].last_message.recipient.message_permission")
                        .value("test message"))
                .andExpect(jsonPath("$.data[0].last_message.message_text").value("Text1"))
                .andExpect(jsonPath("$.data[0].last_message.read_status").value("READ"))
                .andExpect(jsonPath("$.total").value(2));
    }

    @WithAnonymousUser
    @DisplayName("Get dialogs by user test with status 401")
    @Test
    void getDialogsByUser_withStatus401() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/dialogs"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @DisplayName("Start dialog with user test")
    @Test
    void createDialogWithUser() throws Exception {
        List<Integer> userIds = new ArrayList<>(4);
        DialogUserShortListRq rq = new DialogUserShortListRq();
        rq.setUserIds(userIds);
        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/dialogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rq)))
                .andDo(print())
                .andExpectAll(status().isOk());
    }

    @WithAnonymousUser
    @DisplayName("Start dialog with user test with status 401")
    @Test
    void createDialogWithUser_withStatus401() throws Exception {
        List<Integer> userIds = new ArrayList<>(4);
        DialogUserShortListRq rq = new DialogUserShortListRq();
        rq.setUserIds(userIds);
        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/dialogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rq)))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @DisplayName("Get unread messages from dialog test")
    @Test
    void getUnreadMessages() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/dialogs/1/unread"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(3))
                .andExpect(jsonPath("$.data[0].read_status").value("UNREAD"))
                .andExpect(jsonPath("$.total").value(1));
    }

    @WithAnonymousUser
    @DisplayName("Get unread messages from dialog test with status 401")
    @Test
    void getUnreadMessages_withStatus401() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/dialogs/1/unread"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @DisplayName("Get unread messages from dialog test with status 400")
    @Test
    void getUnreadMessages_withStatus400() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/dialogs/10/unread"))
                .andDo(print())
                .andExpectAll(status().isBadRequest());
    }

    @DisplayName("Get messages from dialog test")
    @Test
    void getMessagesByDialogId() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/dialogs/1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("offset", "0")
                        .param("perPage", "20"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[2].id").value(3))
                .andExpect(jsonPath("$.data[0].isSentByMe").value(true))
                .andExpect(jsonPath("$.data[1].isSentByMe").value(true))
                .andExpect(jsonPath("$.data[2].isSentByMe").value(false))
                .andExpect(jsonPath("$.total").value(3));
    }

    @WithAnonymousUser
    @DisplayName("Get messages from dialog test with status 401")
    @Test
    void getMessagesByDialogId_withStatus401() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/dialogs/1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("offset", "0")
                        .param("perPage", "20"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }


    @DisplayName("Get messages from dialog test with status 400")
    @Test
    void getMessagesByDialogId_withStatus400() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/dialogs/100/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("offset", "0")
                        .param("perPage", "20"))
                .andDo(print())
                .andExpectAll(status().isBadRequest());
    }

    @DisplayName("Get count of unread messages")
    @Test
    void getCountUnreadMessages() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/dialogs/unreaded"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.count").value(1));
    }

    @WithAnonymousUser
    @DisplayName("Get count of unread messages with status 401")
    @Test
    void getCountUnreadMessages_withStatus401() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/dialogs/unreaded"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }
}