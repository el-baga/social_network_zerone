package com.skillbox.zerone.controller;

import com.skillbox.zerone.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
class StorageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Upload users profile image with status 401")
    @WithAnonymousUser
    void testUploadImage_withStatus401() throws Exception {
        String type = "IMAGE";
        byte[] file = new byte[1];
        this.mockMvc.perform(post("/api/v1/storage")
                        .param("type", type)
                        .content(file))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
