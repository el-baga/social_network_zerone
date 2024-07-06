package com.skillbox.zerone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.zerone.config.TestConfig;
import com.skillbox.zerone.dto.request.CommentRq;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/commentController-beforeTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/commentController-afterTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    private String createComment(String commentText, Long parentId) throws JsonProcessingException {
        final var req = new CommentRq();
        req.setCommentText(commentText);
        req.setParentId(parentId);
        return new ObjectMapper().writeValueAsString(req);
    }

    @Test
    @Order(1)
    @DisplayName("Добавление комментария к посту")
    @WithMockUser("2")
    void testAddComment_addComment_result200() throws Exception {
        final var postId = 1;
        final var text = "Пиши ещё!!";

        this.mockMvc.perform(
                post(String.format("/api/v1/post/%d/comments", postId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createComment(text, null))
            )
            .andDo(print())
            .andExpectAll(status().isOk())
            .andExpect(jsonPath("$.data.post_id").value(postId))
            .andExpect(jsonPath("$.data.author.id").value(2))
            .andExpect(jsonPath("$.data.comment_text").value(text));
    }

    @Test
    @Order(2)
    @DisplayName("Добавление комментария к комментарию")
    @WithMockUser("1")
    void testAddComment_addCommentToComment_result200() throws Exception {
        final var postId = 1;
        final var parentCommentId = 1L;
        final var text = "Спасибо!";

        this.mockMvc.perform(
                post(String.format("/api/v1/post/%d/comments", postId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createComment(text, parentCommentId))
            )
            .andDo(print())
            .andExpectAll(status().isOk())
            .andExpect(jsonPath("$.data.post_id").value(postId))
            .andExpect(jsonPath("$.data.author.id").value(1))
            .andExpect(jsonPath("$.data.comment_text").value(text))
            .andExpect(jsonPath("$.data.parent_id").value(parentCommentId));
    }

    @Test
    @Order(3)
    @DisplayName("Добавление комментария к посту неавторизованным пользователем")
    @WithAnonymousUser
    void testAddComment_anonymous_result401() throws Exception {
        this.mockMvc.perform(
                post(String.format("/api/v1/post/%d/comments", 1))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpectAll(status().isUnauthorized());
    }

    @Test
    @Order(4)
    @DisplayName("Добавление комментария к несуществующему посту")
    @WithMockUser("2")
    void testAddComment_wrongPostId_result400() throws Exception {
        final var postId = 10;
        final var text = "А поста то нет!";

        this.mockMvc.perform(
                post(String.format("/api/v1/post/%d/comments", postId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createComment(text, null))
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }

    @Test
    @Order(5)
    @DisplayName("Добавление комментария к несуществующему комментарию")
    @WithMockUser("2")
    void testAddComment_wrongCommentId_result400() throws Exception {
        final var postId = 1;
        final var parentCommentId = 20L;
        final var text = "А комментария то нет!";

        this.mockMvc.perform(
                post(String.format("/api/v1/post/%d/comments", postId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createComment(text, parentCommentId))
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }

    @Test
    @Order(6)
    @DisplayName("Добавление комментария с пустым текстом")
    @WithMockUser("2")
    void testAddComment_emptyText_result400() throws Exception {
        final var postId = 1;
        final var text = "";

        this.mockMvc.perform(
                post(String.format("/api/v1/post/%d/comments", postId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createComment(text, null))
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }

    @Test
    @Order(7)
    @DisplayName("Удаление комментария")
    @WithMockUser("3")
    void testSoftDelete_delete_result200() throws Exception {
        final var postId = 1;
        final var commentId = 3;

        this.mockMvc.perform(
                delete(String.format("/api/v1/post/%d/comments/%d", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpectAll(status().isOk())
            .andExpect(jsonPath("$.data.post_id").value(postId))
            .andExpect(jsonPath("$.data.id").value(commentId))
            .andExpect(jsonPath("$.data.author.id").value(3))
            .andExpect(jsonPath("$.data.is_deleted").value(true));
    }

    @Test
    @Order(8)
    @DisplayName("Удаление комментария неавторизованным пользователем")
    @WithAnonymousUser
    void testSoftDelete_anonymous_result401() throws Exception {
        final var postId = 1;
        final var commentId = 3;

        this.mockMvc.perform(
                delete(String.format("/api/v1/post/%d/comments/%d", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpectAll(status().isUnauthorized());
    }

    @Test
    @Order(9)
    @DisplayName("Удаление несуществующего комментария")
    @WithMockUser("3")
    void testSoftDelete_wrongCommentId_result400() throws Exception {
        final var postId = 1;
        final var commentId = 30;

        this.mockMvc.perform(
                delete(String.format("/api/v1/post/%d/comments/%d", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }

    @Test
    @Order(10)
    @DisplayName("Удаление чужого комментария")
    @WithMockUser("3")
    void testSoftDelete_another_result400() throws Exception {
        final var postId = 1;
        final var commentId = 1;

        this.mockMvc.perform(
                delete(String.format("/api/v1/post/%d/comments/%d", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }

    @Test
    @Order(11)
    @DisplayName("Восстановление комментария")
    @WithMockUser("1")
    void testRecover_valid_result200() throws Exception {
        final var postId = 1;
        final var commentId = 4;

        this.mockMvc.perform(
                put(String.format("/api/v1/post/%d/comments/%d/recover", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpectAll(status().isOk())
            .andExpect(jsonPath("$.data.post_id").value(postId))
            .andExpect(jsonPath("$.data.id").value(commentId))
            .andExpect(jsonPath("$.data.author.id").value(1))
            .andExpect(jsonPath("$.data.is_deleted").value(false));
    }

    @Test
    @Order(12)
    @DisplayName("Восстановление комментария неавторизованным пользователем")
    @WithAnonymousUser
    void testRecover_anonymous_result401() throws Exception {
        final var postId = 1;
        final var commentId = 4;

        this.mockMvc.perform(
                put(String.format("/api/v1/post/%d/comments/%d/recover", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpectAll(status().isUnauthorized());
    }

    @Test
    @Order(13)
    @DisplayName("Восстановление несуществующего комментария")
    @WithMockUser("1")
    void testRecover_wrongCommentId_result400() throws Exception {
        final var postId = 1;
        final var commentId = 30;

        this.mockMvc.perform(
                put(String.format("/api/v1/post/%d/comments/%d/recover", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }

    @Test
    @Order(14)
    @DisplayName("Восстановление чужого комментария")
    @WithMockUser("3")
    void testRecover_another_result400() throws Exception {
        final var postId = 1;
        final var commentId = 4;

        this.mockMvc.perform(
                put(String.format("/api/v1/post/%d/comments/%d/recover", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }

    @Test
    @Order(15)
    @DisplayName("Редактирование комментария")
    @WithMockUser("1")
    void testUpdateComment_validData_result200() throws Exception {
        final var postId = 1;
        final var commentId = 2;
        final var text = "Новый текст";

        this.mockMvc.perform(
                put(String.format("/api/v1/post/%d/comments/%d", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createComment(text, null))
            )
            .andDo(print())
            .andExpectAll(status().isOk())
            .andExpect(jsonPath("$.data.post_id").value(postId))
            .andExpect(jsonPath("$.data.author.id").value(1))
            .andExpect(jsonPath("$.data.comment_text").value(text));
    }

    @Test
    @Order(16)
    @DisplayName("Редактирование комментария неавторизованным пользователем")
    @WithAnonymousUser
    void testUpdateComment_anonymous_result401() throws Exception {
        final var postId = 1;
        final var commentId = 2;
        final var text = "Новый текст";

        this.mockMvc.perform(
                put(String.format("/api/v1/post/%d/comments/%d", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createComment(text, null))
            )
            .andDo(print())
            .andExpectAll(status().isUnauthorized());
    }

    @Test
    @Order(17)
    @DisplayName("Редактирование несуществующего комментария")
    @WithMockUser("2")
    void testUpdateComment_wrongCommentId_result400() throws Exception {
        final var postId = 1;
        final var commentId = 30;
        final var text = "Такого комментария нет";

        this.mockMvc.perform(
                put(String.format("/api/v1/post/%d/comments/%d", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createComment(text, 15L))
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }

    @Test
    @Order(18)
    @DisplayName("Редактирование комментария пустым текстом")
    @WithMockUser("1")
    void testUpdateComment_emptyText_result400() throws Exception {
        final var postId = 1;
        final var commentId = 2;
        final var text = "";

        this.mockMvc.perform(
                put(String.format("/api/v1/post/%d/comments/%d", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createComment(text, null))
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }

    @Test
    @Order(19)
    @DisplayName("Редактирование чужого комментария")
    @WithMockUser("3")
    void testUpdateComment_another_result400() throws Exception {
        final var postId = 1;
        final var commentId = 1;
        final var text = "Чужой комментарий";

        this.mockMvc.perform(
                put(String.format("/api/v1/post/%d/comments/%d", postId, commentId))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createComment(text, null))
            )
            .andDo(print())
            .andExpectAll(status().isBadRequest());
    }

    @Test
    @Order(20)
    @DisplayName("Получение списка комментариев")
    @WithMockUser("1")
    void testGetComments_valid_result200() throws Exception {
        final var postId = 1;
        final var total = 3;

        this.mockMvc
            .perform(get(String.format("/api/v1/post/%d/comments", postId)))
            .andDo(print())
            .andExpectAll(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(total))
            .andExpect(jsonPath("$.total").value(total));
    }

    @Test
    @Order(21)
    @DisplayName("Получение списка комментариев неавторизованным пользователем")
    @WithAnonymousUser
    void testGetComments_anonymous_result401() throws Exception {
        final var postId = 1;

        this.mockMvc
            .perform(get(String.format("/api/v1/post/%d/comments", postId)))
            .andDo(print())
            .andExpectAll(status().isUnauthorized());
    }
}
