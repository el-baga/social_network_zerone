package com.skillbox.zerone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.zerone.config.TestConfig;
import com.skillbox.zerone.dto.request.PersonRq;
import com.skillbox.zerone.dto.request.PostRq;
import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.repository.PersonRepository;
import com.skillbox.zerone.service.PersonService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Calendar;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(value = "/sql/personController-beforeTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/sql/personController-afterTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Test
    @Order(1)
    @DisplayName("Get information about me with status 200")
    @WithMockUser("3")
    void testAboutMe_withStatus200() throws Exception {
        this.mockMvc.perform(get("/api/v1/users/me"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.first_name").value("Yman"))
                .andExpect(jsonPath("$.data.last_name").value("Back"))
                .andExpect(jsonPath("$.data.phone").value("78333124779"))
                .andExpect(jsonPath("$.data.email").value("ymanchik@back.ru"));
    }

    @Test
    @Order(2)
    @DisplayName("Get information about me with status 401")
    @WithAnonymousUser
    void testAboutMe_withStatus401() throws Exception {
        this.mockMvc.perform(get("/api/v1/users/me"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(3)
    @DisplayName("Create a new post with status 200")
    @WithMockUser("1")
    void testCreatePost_withStatus200() throws Exception {
        String title = "Как я провел свой день...";
        String postText = "Спортзал, программирование и просмотр сериала за кружкой какао!";
        List<String> tags = List.of("спорт", "программирование");

        this.mockMvc.perform(post("/api/v1/users/1/wall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getPostRq(title, postText, tags)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.post_text").value(postText))
                .andExpect(jsonPath("$.data.tags[0]").value(tags.get(0)))
                .andExpect(jsonPath("$.data.tags[1]").value(tags.get(1)))
                .andExpect(jsonPath("$.data.title").value(title));
    }

    @Test
    @Order(4)
    @DisplayName("Create a new post with status 401")
    @WithAnonymousUser
    void testCreatePost_withStatus401() throws Exception {
        this.mockMvc.perform(post("/api/v1/users/1/wall"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(5)
    @DisplayName("Create a new post with status 400 (incorrect author id in path)")
    @WithMockUser("1")
    void testCreatePost_withStatus400IncorrectAuthorId() throws Exception {
        this.mockMvc.perform(post("/api/v1/users/10/wall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getPostRq("Title", "PostText", null)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    @DisplayName("Create a new post with status 400 (publish date before current time)")
    @WithMockUser("1")
    void testCreatePost_withStatus400DateBeforeCurrentTime() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        long newTimestamp = calendar.getTimeInMillis();
        this.mockMvc.perform(post("/api/v1/users/1/wall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getPostRq("Title", "PostText", null))
                        .param("publish_date", String.valueOf(newTimestamp)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    @DisplayName("Get all posts by author id with status 200")
    @WithMockUser("1")
    void testGetPosts_withStatus200() throws Exception {
        this.mockMvc.perform(get("/api/v1/users/1/wall").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("Hello"))
                .andExpect(jsonPath("$.data[1].title").value("Happy holidays"));
    }

    @Test
    @Order(8)
    @DisplayName("Get all posts by author id with status 401")
    @WithAnonymousUser
    void testGetPosts_withStatus401() throws Exception {
        this.mockMvc.perform(get("/api/v1/users/1/wall"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(9)
    @DisplayName("Search specific user by query with status 200")
    @WithMockUser("1")
    void testSearchSpecificPerson_withStatus200() throws Exception {
        String firstName = "Bob";
        String lastName = "Martin";

        this.mockMvc.perform(get("/api/v1/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("first_name", firstName)
                        .param("last_name", lastName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].first_name").value(firstName))
                .andExpect(jsonPath("$.data[0].last_name").value(lastName))
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    @Order(10)
    @DisplayName("Search users by query with status 200")
    @WithMockUser("1")
    void testSearchPersons_withStatus200() throws Exception {
        int ageFrom = 15;
        int ageTo = 45;

        this.mockMvc.perform(get("/api/v1/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ageFrom", String.valueOf(ageFrom))
                        .param("ageTo", String.valueOf(ageTo)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(2));
    }

    @Test
    @Order(11)
    @DisplayName("Search users by query with status 401")
    @WithAnonymousUser
    void testSearchPersons_withStatus401() throws Exception {
        this.mockMvc.perform(get("/api/v1/users/search"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(12)
    @DisplayName("Update information about me with status 200")
    @WithMockUser("1")
    void testUpdateMyInfo_withStatus200() throws Exception {
        String about = "I ma java dev baby";
        String phone = "79312787500";
        Person person = personService.getPersonById(1L);
        person.setAbout(about);
        person.setPhone(phone);
        PersonRq rq = new PersonRq();
        rq.setFirstName(person.getFirstName());
        rq.setLastName(person.getLastName());
        rq.setAbout(about);
        rq.setPhone(phone);

        this.mockMvc.perform(put("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(rq)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.about").value(about))
                .andExpect(jsonPath("$.data.phone").value(phone));
    }

    @Test
    @Order(13)
    @DisplayName("Update information about me with status 400 (Empty first and last name")
    @WithMockUser("1")
    void testUpdateMyInfo_withStatus400EmptyFirstAndLastName() throws Exception {
        String firstName = "";
        String lastName = "";
        Person person = personService.getPersonById(1L);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        PersonRq rq = new PersonRq();

        this.mockMvc.perform(put("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(rq)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(14)
    @DisplayName("Update information about me with status 401")
    @WithAnonymousUser
    void testUpdateMyInfo_withStatus401() throws Exception {
        this.mockMvc.perform(put("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new PersonRq())))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(15)
    @DisplayName("Get user by id with status 200")
    @WithMockUser("1")
    void testGetPersonById_withStatus200() throws Exception {
        long id = 2L;
        Person person = personService.getPersonById(id);

        this.mockMvc.perform(get(String.format("/api/v1/users/%d", id)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.first_name").value(person.getFirstName()))
                .andExpect(jsonPath("$.data.last_name").value(person.getLastName()))
                .andExpect(jsonPath("$.data.friend_status").value("UNKNOWN"));
    }

    @Test
    @Order(16)
    @DisplayName("Get user by id with status 401")
    @WithAnonymousUser
    void testGetPersonById_withStatus401() throws Exception {
        this.mockMvc.perform(get(String.format("/api/v1/users/%d", 2L)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(17)
    @DisplayName("Delete information about me with status 200")
    @WithMockUser("1")
    void testSoftDeleteInformationAboutMe_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/users/me");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        boolean isDeleted = personRepository.existsByIdAndIsDeleted(1L, true);
        Assertions.assertTrue(isDeleted);
    }

    @Test
    @Order(18)
    @DisplayName("Delete information about me with status 401")
    @WithAnonymousUser
    void testSoftDeleteInformationAboutMe_withStatus401() throws Exception {
        this.mockMvc.perform(delete("/api/v1/users/me"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(19)
    @DisplayName("Recover information about me with status 200")
    @WithMockUser("4")
    void testRecoverAboutMe_withStatus200() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/users/me/recover");
        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        boolean isDeleted = personRepository.existsByIdAndIsDeleted(4L, false);
        Assertions.assertTrue(isDeleted);
    }

    @Test
    @Order(20)
    @DisplayName("Recover information about me with status 401")
    @WithAnonymousUser
    void testRecoverAboutMe_withStatus401() throws Exception {
        this.mockMvc.perform(post("/api/v1/users/me/recover"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    private String getPostRq(String title, String postText, List<String> tags) throws JsonProcessingException {
        PostRq postRq = new PostRq();
        postRq.setTitle(title);
        postRq.setPostText(postText);
        postRq.setTags(tags);
        return new ObjectMapper().writeValueAsString(postRq);
    }
}
