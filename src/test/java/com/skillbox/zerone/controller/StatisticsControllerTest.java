package com.skillbox.zerone.controller;

import com.skillbox.zerone.config.TestConfig;
import com.skillbox.zerone.service.StatisticsService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(value = "/sql/statisticsController-beforeTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/sql/statisticsController-afterTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StatisticsService statisticsService;

    @Test
    @Order(1)
    @DisplayName("Get the number of all users with status 200")
    @WithMockUser("1")
    void testGetUserCount_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/user");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Long expected = 5L;
        Long actual = statisticsService.getUserCount();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(2)
    @DisplayName("Get the number of blocked users with status 200")
    @WithMockUser("1")
    void testGetBlockedUserCount_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/user/blocked");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Long expected = 2L;
        Long actual = statisticsService.getBlockedUserCount();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(3)
    @DisplayName("Get the number of deleted users with status 200")
    @WithMockUser("1")
    void testGetSoftDeletedUserCount_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/user/deleted");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Long expected = 1L;
        Long actual = statisticsService.getSoftDeletedUserCount();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(4)
    @DisplayName("Get the number of all users by country name with status 200")
    @WithMockUser("1")
    void testGetUserCountByCounty_withStatus200() throws Exception {
        String country = "Russia";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/user/country")
                .param("country", country);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Long expected = 5L;
        Long actual = statisticsService.getUserCountByCountry(country);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(5)
    @DisplayName("Get the number of all users by city name with status 200")
    @WithMockUser("1")
    void testGetUserCountByCity_withStatus200() throws Exception {
        String city = "Saint-Petersburg";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/user/city")
                .param("city", city);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Long expected = 2L;
        Long actual = statisticsService.getUserCountByCity(city);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(6)
    @DisplayName("Get the number of all tags with status 200")
    @WithMockUser("1")
    void testGetTagCount_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/tag");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Long expected = 6L;
        Long actual = statisticsService.getTagCount();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Order(7)
    @DisplayName("Get the number of tags by post id with status 200")
    @WithMockUser("1")
    void testGetTagCountByPostId_withStatus200() throws Exception {
        Long postId = 2L;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/tag/post")
                .param("post_id", String.valueOf(postId));
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$").value(2L));
    }

    @Test
    @Order(8)
    @DisplayName("Get the number of all posts with status 200")
    @WithMockUser("1")
    void testGetPostCount_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/post");
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$").value(2L));
    }

    @Test
    @Order(9)
    @DisplayName("Get the number of post by user id with status 200")
    @WithMockUser("1")
    void testGetPostCountByUserId_withStatus200() throws Exception {
        Long userId = 1L;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/post/user")
                .param("user_id", String.valueOf(userId));
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$").value(1L));
    }

    @Test
    @Order(10)
    @DisplayName("Get the number of all messages with status 200")
    @WithMockUser("1")
    void testGetMessageCount_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/message");
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$").value(3L));
    }

    @Test
    @Order(11)
    @DisplayName("Get the number of all messages by dialog id with status 200")
    @WithMockUser("1")
    void testGetMessageCountByDialogId_withStatus200() throws Exception {
        Long dialogId = 1L;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/message/dialog")
                .param("dialog_id", String.valueOf(dialogId));
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$").value(3L));
    }

    @Test
    @Order(12)
    @DisplayName("Get the number of messages by id's of two persons with status 200")
    @WithMockUser("1")
    void testGetMessageByIdOfTwoPersons_withStatus200() throws Exception {
        Long firstUserId = 1L;
        Long secondUserId = 2L;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/message/all")
                .param("first_user_id", String.valueOf(firstUserId))
                .param("second_user_id", String.valueOf(secondUserId));
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.Baga").value(2L));
    }

    @Test
    @Order(13)
    @DisplayName("Get the number of all likes with status 200")
    @WithMockUser("1")
    void testGetLikeCount_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/like");
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$").value(4L));
    }

    @Test
    @Order(14)
    @DisplayName("Get the number of likes by post or comment id with status 200")
    @WithMockUser("1")
    void testGetLikeCountByEntityId_withStatus200() throws Exception {
        Long commentId = 1L;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/like/entity")
                .param("comment_id", String.valueOf(commentId));
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$").value(3L));
    }

    @Test
    @Order(15)
    @DisplayName("Get the number of all dialogs with status 200")
    @WithMockUser("1")
    void testGetDialogCount_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/dialog");
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$").value(2L));
    }

    @Test
    @Order(16)
    @DisplayName("Get the number of dialogs by user id with status 200")
    @WithMockUser("1")
    void testGetDialogCountByUserId_withStatus200() throws Exception {
        Long userId = 1L;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/dialog/user")
                .param("user_id", String.valueOf(userId));
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$").value(2L));
    }

    @Test
    @Order(17)
    @DisplayName("Get the number of all countries with status 200")
    @WithMockUser("1")
    void testGetCountryCount_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/country");
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$").value(2L));
    }

    @Test
    @Order(18)
    @DisplayName("Get countries with number of all users with status 200")
    @WithMockUser("1")
    void testGetUsersCountInEachCountry_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/country/all");
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.[0].countUsers").value(5L));
    }

    @Test
    @Order(19)
    @DisplayName("Get the number of comments by post id with status 200")
    @WithMockUser("1")
    void testGetCommentCountByPostId_withStatus200() throws Exception {
        Long postId = 1L;
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/comment/post")
                .param("post_id", String.valueOf(postId));
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$").value(2L));
    }

    @Test
    @Order(20)
    @DisplayName("Get the number of all cities with status 200")
    @WithMockUser("1")
    void testGetCityCount_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/city");
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$").value(2L));
    }

    @Test
    @Order(21)
    @DisplayName("Get cities with number of users with status 200")
    @WithMockUser("1")
    void testGetUsersCountInEachCity_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/statistics/city/all");
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(jsonPath("$.[0].countUsers").value(1L));
    }
}
