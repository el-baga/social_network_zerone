package com.skillbox.zerone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.zerone.config.TestConfig;
import com.skillbox.zerone.dto.request.LikeRq;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/likeController-beforeTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/likeController-afterTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class LikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    private String createLike(Long postId, Long itemId, boolean isPostLike) throws JsonProcessingException {
        final var req = new LikeRq();
        req.setItemId(itemId);
        req.setPostId(postId);
        req.setType(isPostLike ? "Post" : "Comment");
        return new ObjectMapper().writeValueAsString(req);
    }

    @Test
    @Order(1)
    @DisplayName("Получение лайков к посту")
    @WithMockUser("2")
    void testGetLikes_valid_result200() throws Exception {
        final var postId = "1";
        final var likeCount = 1;

        this.mockMvc
            .perform(get("/api/v1/likes").param("item_id", postId))
            .andDo(print())
            .andExpectAll(status().isOk())
            .andExpect(jsonPath("$.data.likes").value(likeCount));
    }

    @Test
    @Order(2)
    @DisplayName("Добавление лайка к посту")
    @WithMockUser("2")
    void testAddLike_addToPost_result200() throws Exception {
        final var postId = 1L;
        final var itemId = 1L;

        this.mockMvc.perform(
                put("/api/v1/likes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createLike(postId, itemId, true))
            )
            .andDo(print())
            .andExpectAll(status().isOk());
    }

    @Test
    @Order(3)
    @DisplayName("Добавление лайка к комментарию")
    @WithMockUser("2")
    void testAddLike_addToComment_result200() throws Exception {
        final var postId = 2L;
        final var itemId = 2L;

        this.mockMvc.perform(
                put("/api/v1/likes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createLike(postId, itemId, false))
            )
            .andDo(print())
            .andExpectAll(status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("Повторное добавление лайка к посту")
    @WithMockUser("3")
    void testAddLike_addToPost_result400() throws Exception {
        final var postId = 1L;
        final var itemId = 1L;

        this.mockMvc.perform(
                put("/api/v1/likes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createLike(postId, itemId, false))
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }

    @Test
    @Order(5)
    @DisplayName("Добавление лайка к комментарию без Post ID")
    @WithMockUser("2")
    void testAddLike_addToComment_result400() throws Exception {
        final var itemId = 1L;

        this.mockMvc.perform(
                put("/api/v1/likes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createLike(null, itemId, false))
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }

    @Test
    @Order(6)
    @DisplayName("Удаление лайка")
    @WithMockUser("3")
    void testRemoveLike_delete_result200() throws Exception {
        final var postId = 1L;
        final var itemId = 1L;

        this.mockMvc.perform(
                delete("/api/v1/likes")
                    .param("item_id", String.valueOf(itemId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createLike(postId, itemId, true))
            )
            .andDo(print())
            .andExpectAll(status().isOk());
    }

    @Test
    @Order(7)
    @DisplayName("Удаление несуществующего лайка")
    @WithMockUser("1")
    void testSoftDelete_delete_result400() throws Exception {
        final var postId = 1L;
        final var itemId = 1L;

        this.mockMvc.perform(
                delete("/api/v1/likes")
                    .param("item_id", String.valueOf(itemId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createLike(postId, itemId, true))
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }
}
