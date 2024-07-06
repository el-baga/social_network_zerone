package com.skillbox.zerone.controller;


import com.skillbox.zerone.config.TestConfig;
import com.skillbox.zerone.dto.request.PostUpdateRq;
import com.skillbox.zerone.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/insert.sql")
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@AutoConfigureMockMvc
@WithMockUser(username = "1", authorities = {"USER"})
class PostControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MockMvc mockMvc;

    @DisplayName("Get all news test with status 200")
    @Test
    void getFeedsTest_withStatus200() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/feeds"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("title1"))
                .andExpect(jsonPath("$.data[0].type").value("POSTED"))
                .andExpect(jsonPath("$.data[1].title").value("title3"))
                .andExpect(jsonPath("$.data[1].type").value("POSTED"))
                .andExpect(jsonPath("$.data[2].title").value("title2"))
                .andExpect(jsonPath("$.data[2].type").value("POSTED"))
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.length()").value(6));
    }

    @WithAnonymousUser
    @DisplayName("Get all news test with status 401")
    @Test
    void getFeedsTest_withStatus401() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/feeds"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @DisplayName("Update post test with status 200")
    @Test
    void updatePostTest_withStatus200() throws Exception {
        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostUpdateRq()))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.post_text").value("text11"))
                .andExpect(jsonPath("$.data.tags[0]").value("tag11"))
                .andExpect(jsonPath("$.data.title").value("title11"))
                .andExpect(jsonPath("$.data.type").value("POSTED"));
    }

    @DisplayName("Update post test with status 400")
    @Test
    void updatePostTest_withStatus400() throws Exception {
        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/post/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostUpdateRq()))
                .andDo(print())
                .andExpectAll(status().isBadRequest());
    }

    @WithAnonymousUser
    @DisplayName("Update post test with status 401")
    @Test
    void updatePostTest_withStatus401() throws Exception {
        System.out.println(createPostUpdateRq());
        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPostUpdateRq()))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @DisplayName("Get post test with status 200")
    @Test
    void getPostTest_withStatus200() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/post/1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.title").value("title1"))
                .andExpect(jsonPath("$.data.type").value("POSTED"));
    }

    @WithAnonymousUser
    @DisplayName("Get post test with status 401")
    @Test
    void getPostTest_withStatus401() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/post/1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @DisplayName("Get post test with status 400")
    @Test
    void getPostTest_withStatus400() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/post/15"))
                .andDo(print())
                .andExpectAll(status().isBadRequest());
    }

    private String createPostUpdateRq() throws JsonProcessingException {
        List<String> tagList = new ArrayList<>();
        tagList.add("tag11");
        PostUpdateRq postUpdateRq = PostUpdateRq.builder().title("title11").postText("text11").tags(tagList).build();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(postUpdateRq);
    }

    @DisplayName("Soft delete test with status 200")
    @Test
    @WithMockUser("2")
    void softDeleteTest_withStatus200() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/post/2"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.type").value("DELETED"));
    }

    @DisplayName("Soft delete test with status 400")
    @Test
    void softDeleteTest_withStatus400() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/post/10"))
                .andDo(print())
                .andExpectAll(status().isBadRequest());
    }

    @WithAnonymousUser
    @DisplayName("Soft delete test with status 401")
    @Test
    void softDeleteTest_withStatus401() throws Exception {
        this.mockMvc.perform(delete("http://localhost:" + port + "/api/v1/post/2"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @DisplayName("Recover post test with status 200")
    @Test
    @WithMockUser("3")
    void recoverPostTest_withStatus200() throws Exception {
        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/post/6/recover"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data.id").value(6))
                .andExpect(jsonPath("$.data.type").value("POSTED"));
    }

    @DisplayName("Recover post test with status 400")
    @Test
    void recoverPostTest_withStatus400() throws Exception {
        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/post/10/recover"))
                .andDo(print())
                .andExpectAll(status().isBadRequest());
    }

    @WithAnonymousUser
    @DisplayName("Recover post test with status 401")
    @Test
    void recoverPostTest_withStatus401() throws Exception {
        this.mockMvc.perform(put("http://localhost:" + port + "/api/v1/post/2/recover"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }


    @DisplayName("Search post test with status 200 and params: text, date, author")
    @Test
    void searchPostsTest_withStatus200_paramsTextDateAuthor() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "text1")
                        .param("date_from","1674986450057")
                        .param("date_to","1706520387688")
                        .param("author", "Name1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("title1"))
                .andExpect(jsonPath("$.data[0].post_text").value("text1"))
                .andExpect(jsonPath("$.total").value(1));
    }

    @DisplayName("Search post test with status 200 and params: text, date, author and one tag")
    @Test
    void searchPostsTest_withStatus200_paramsTextDateAuthorAndOneTag() throws Exception {
        List<String> tagList = new ArrayList<>();
        tagList.add("tag1");
        tagList.add("tag2");
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "text1")
                        .param("date_from","1674986450057")
                        .param("date_to","1706520387688")
                        .param("tags", "tag2")
                        .param("author", "Name1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("title1"))
                .andExpect(jsonPath("$.data[0].post_text").value("text1"))
                .andExpect(jsonPath("$.data[0].tags").value(tagList))
                .andExpect(jsonPath("$.total").value(1));
    }

    @DisplayName("Search post test with status 200 and params: text, date, author and list tag")
    @Test
    void searchPostsTest_withStatus200_paramsTextDateAuthorAndListTag() throws Exception {
        List<String> tagList = new ArrayList<>();
        tagList.add("tag1");
        tagList.add("tag2");
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "text1")
                        .param("date_from","1674986450057")
                        .param("date_to","1706520387688")
                        .param("tags", "tag1, tag2")
                        .param("author", "Name1"))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("title1"))
                .andExpect(jsonPath("$.data[0].post_text").value("text1"))
                .andExpect(jsonPath("$.data[0].tags").value(tagList))
                .andExpect(jsonPath("$.total").value(1));
    }

    @WithAnonymousUser
    @DisplayName("Search post test with status 401")
    @Test
    void searchPostsTest_withStatus401() throws Exception {
        this.mockMvc.perform(get("http://localhost:" + port + "/api/v1/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "text1")
                        .param("date_from","1674986450057")
                        .param("date_to","1706520387688")
                        .param("author", "Name1"))
                .andDo(print())
                .andExpectAll(status().isUnauthorized());
    }

    @DisplayName("Search post with empty params")
    @Test
    void searchPostsTest_getAllPosts() throws Exception {
        this.mockMvc
            .perform(get("http://localhost:" + port + "/api/v1/post").contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.*", hasSize(3)));
    }
}
