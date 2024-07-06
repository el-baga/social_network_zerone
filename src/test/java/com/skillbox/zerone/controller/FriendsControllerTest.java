package com.skillbox.zerone.controller;

import com.skillbox.zerone.config.TestConfig;
import com.skillbox.zerone.entity.Friendships;
import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.repository.FriendshipsRepository;
import com.skillbox.zerone.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup({
        @Sql(value = "/sql/friendsController-beforeTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/friendsController-afterTest.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfig.class)
@AutoConfigureMockMvc
class FriendsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FriendshipsRepository friendshipsRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    @DisplayName("Friendship request can be created")
    @WithMockUser("1")
    void testSendFriendshipRequest_whenValidDstUserId_createsAFriendship() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/friends/2");

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Person srcPerson = personRepository.findById(1L).orElseThrow(() -> new BadRequestException("src user not found"));
        Person dstPerson = personRepository.findById(2L).orElseThrow(() -> new BadRequestException("dst user not found"));
        Friendships friendshipsRequest = null;
        Friendships friendshipsRequestRecieved = null;
        Optional<Friendships> friendshipsRequestOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(srcPerson, dstPerson);
        Optional<Friendships> friendshipsRequestRecievedOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(dstPerson, srcPerson);
        if (friendshipsRequestOptional.isPresent()) {
            friendshipsRequest = friendshipsRequestOptional.get();
        }
        if (friendshipsRequestRecievedOptional.isPresent()) {
            friendshipsRequestRecieved = friendshipsRequestRecievedOptional.get();
        }
        Assertions.assertNotNull(friendshipsRequest);
        Assertions.assertNotNull(friendshipsRequestRecieved);
        Assertions.assertEquals("REQUEST", friendshipsRequest.getStatusName());
        Assertions.assertEquals("RECEIVED_REQUEST", friendshipsRequestRecieved.getStatusName());
    }

    @Test
    @DisplayName("Friendship request is not created when invalid dst user id provided")
    @WithMockUser("1")
    void testSendFriendshipRequest_whenInvalidDstUserId_friendshipIsNotCreated() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/friends/9");

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());

        Person srcPerson = personRepository.getPersonById(1L);
        Person notExistingPerson = new Person();
        notExistingPerson.setId(3L);
        Friendships friendshipsRequest = null;
        Friendships friendshipsRequestRecieved = null;
        Optional<Friendships> friendshipsRequestOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(srcPerson, notExistingPerson);
        Optional<Friendships> friendshipsRequestRecievedOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(notExistingPerson, srcPerson);
        if (friendshipsRequestOptional.isPresent()) {
            friendshipsRequest = friendshipsRequestOptional.get();
        }
        if (friendshipsRequestRecievedOptional.isPresent()) {
            friendshipsRequestRecieved = friendshipsRequestRecievedOptional.get();
        }
        Assertions.assertNull(friendshipsRequest);
        Assertions.assertNull(friendshipsRequestRecieved);

    }

    @Test
    @DisplayName("Bad request is thrown when deleting friend with invalid dst user id")
    @WithMockUser("3")
    void testDeleteFriendById_whenInvalidDstUserId_badRequestIsThrown() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/friends/99");

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Friendship can be deleted")
    @WithMockUser("3")
    void testDeleteFriendById_whenValidDstUserId_friendshipIsDeleted() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/friends/4");

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Person srcPerson = personRepository.getPersonById(3L);
        Person dstPerson = personRepository.getPersonById(4L);
        Friendships friendships = null;
        Optional<Friendships> friendshipsOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(srcPerson, dstPerson);
        if (friendshipsOptional.isPresent()) {
            friendships = friendshipsOptional.get();
        }
        Assertions.assertNull(friendships);
    }

    @Test
    @DisplayName("Friend can be added")
    @WithMockUser("6")
    void testAddFriendById_whenValidDstUserId_changesFriendshipsStatusToFriend() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/friends/request/5");

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Person srcPerson = personRepository.findById(5L).orElseThrow(() -> new BadRequestException("src user not found"));
        Person dstPerson = personRepository.findById(6L).orElseThrow(() -> new BadRequestException("dst user not found"));
        Friendships friendshipsRequest = null;
        Friendships friendshipsRequestRecieved = null;
        Optional<Friendships> friendshipsRequestOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(srcPerson, dstPerson);
        Optional<Friendships> friendshipsRequestRecievedOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(dstPerson, srcPerson);
        if (friendshipsRequestOptional.isPresent()) {
            friendshipsRequest = friendshipsRequestOptional.get();
        }
        if (friendshipsRequestRecievedOptional.isPresent()) {
            friendshipsRequestRecieved = friendshipsRequestRecievedOptional.get();
        }
        Assertions.assertNotNull(friendshipsRequest);
        Assertions.assertNotNull(friendshipsRequestRecieved);
        Assertions.assertEquals("FRIEND", friendshipsRequest.getStatusName());
        Assertions.assertEquals("FRIEND", friendshipsRequestRecieved.getStatusName());
    }

    @Test
    @DisplayName("Friend is not added when invalid dst user id")
    @WithMockUser("6")
    void testAddFriendById_whenInvalidDstUserId_changesFriendshipsStatusToFriend() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/friends/request/9");

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());

        Person srcPerson = new Person();
        srcPerson.setId(9L);
        Person dstPerson = personRepository.findById(6L).orElseThrow(() -> new BadRequestException("dst user not found"));
        Friendships friendshipsRequest = null;
        Friendships friendshipsRequestRecieved = null;
        Optional<Friendships> friendshipsRequestOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(srcPerson, dstPerson);
        Optional<Friendships> friendshipsRequestRecievedOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(dstPerson, srcPerson);
        if (friendshipsRequestOptional.isPresent()) {
            friendshipsRequest = friendshipsRequestOptional.get();
        }
        if (friendshipsRequestRecievedOptional.isPresent()) {
            friendshipsRequestRecieved = friendshipsRequestRecievedOptional.get();
        }
        Assertions.assertNull(friendshipsRequest);
        Assertions.assertNull(friendshipsRequestRecieved);
    }

    @Test
    @DisplayName("User can cancel outgoing friendship request")
    @WithMockUser("5")
    void testDeclineOutgoingFriendshipRequestById_whenValidDstUserId_deletesFriendship() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/friends/request/6");

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Person srcPerson = personRepository.findById(5L).orElseThrow(() -> new BadRequestException("dst user not found"));
        Person dstPerson = personRepository.findById(6L).orElseThrow(() -> new BadRequestException("dst user not found"));
        Friendships friendshipsRequest = null;
        Friendships friendshipsRequestRecieved = null;
        Optional<Friendships> friendshipsRequestOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(srcPerson, dstPerson);
        Optional<Friendships> friendshipsRequestRecievedOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(dstPerson, srcPerson);
        if (friendshipsRequestOptional.isPresent()) {
            friendshipsRequest = friendshipsRequestOptional.get();
        }
        if (friendshipsRequestRecievedOptional.isPresent()) {
            friendshipsRequestRecieved = friendshipsRequestRecievedOptional.get();
        }
        Assertions.assertNull(friendshipsRequest);
        Assertions.assertNull(friendshipsRequestRecieved);
    }

    @Test
    @DisplayName("User can cancel incoming friendship request")
    @WithMockUser("6")
    void testDeclineIncomingFriendshipRequestById_whenValidDstUserId_deletesFriendship() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/friends/request/5");

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Person srcPerson = personRepository.findById(5L).orElseThrow(() -> new BadRequestException("dst user not found"));
        Person dstPerson = personRepository.findById(6L).orElseThrow(() -> new BadRequestException("dst user not found"));
        Friendships friendshipsRequest = null;
        Friendships friendshipsRequestRecieved = null;
        Optional<Friendships> friendshipsRequestOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(srcPerson, dstPerson);
        Optional<Friendships> friendshipsRequestRecievedOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(dstPerson, srcPerson);
        if (friendshipsRequestOptional.isPresent()) {
            friendshipsRequest = friendshipsRequestOptional.get();
        }
        if (friendshipsRequestRecievedOptional.isPresent()) {
            friendshipsRequestRecieved = friendshipsRequestRecievedOptional.get();
        }
        Assertions.assertNull(friendshipsRequest);
        Assertions.assertNull(friendshipsRequestRecieved);
    }

    @Test
    @DisplayName("Bad request is thrown when declining friendship with invalid dst user id")
    @WithMockUser("5")
    void testDeclineFriendshipRequestById_whenInvalidDstUserId_badRequestIsThrown() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/friends/request/9");

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("User can block another user")
    @WithMockUser("3")
    void testBlockUnblockFriendById_whenValidDstUserId_blocksUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/friends/block_unblock/4");

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Person srcPerson = personRepository.findById(3L).orElseThrow(() -> new BadRequestException("dst user not found"));
        Person dstPerson = personRepository.findById(4L).orElseThrow(() -> new BadRequestException("dst user not found"));
        Friendships friendships = null;
        Optional<Friendships> friendshipsRequestOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(srcPerson, dstPerson);
        if (friendshipsRequestOptional.isPresent()) {
            friendships = friendshipsRequestOptional.get();
        }
        Assertions.assertNotNull(friendships);
        Assertions.assertEquals("BLOCKED", friendships.getStatusName());
    }

    @Test
    @DisplayName("User can block another user")
    @WithMockUser("1")
    void testBlockUnblockFriendById_whenValidDstUserId_unblocksUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/friends/block_unblock/7");

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Person srcPerson = personRepository.findById(1L).orElseThrow(() -> new BadRequestException("dst user not found"));
        Person dstPerson = personRepository.findById(7L).orElseThrow(() -> new BadRequestException("dst user not found"));
        Friendships friendships = null;
        Optional<Friendships> friendshipsRequestOptional = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(srcPerson, dstPerson);
        if (friendshipsRequestOptional.isPresent()) {
            friendships = friendshipsRequestOptional.get();
        }
        Assertions.assertNotNull(friendships);
        Assertions.assertEquals("FRIEND", friendships.getStatusName());
    }

    @Test
    @DisplayName("Bad request is thrown when blocking/unblocking with invalid dst user id")
    @WithMockUser("1")
    void testBlockUnblockFriendById_whenInvalidDstUserId_badRequestIsThrown() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/friends/block_unblock/9");

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("User can get friend list")
    @WithMockUser("20")
    void testGetFriends_returnsListOfFriends() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/friends");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", anyOf(is(21), is(22))))
                .andExpect(jsonPath("$.data[1].id", anyOf(is(21), is(22))));
    }

    @Test
    @DisplayName("User can get friend request list")
    @WithMockUser("6")
    void testGetFriendsRequest_returnsListOfFriendRequests() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/friends/request");

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(5)));
    }

    @Test
    @DisplayName("User can get recommended friends list when he has no friends")
    @WithMockUser("23")
    void testGetFriendsRecommendation_whenUserHasNoFriends_returnsFriendsRecommendationList() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/friends/recommendations");

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(10)))
                .andExpect(jsonPath("$.data[0].id", anyOf(is(24), is(25), is(26))))
                .andExpect(jsonPath("$.data[1].id", anyOf(is(24), is(25), is(26))))
                .andExpect(jsonPath("$.data[2].id", anyOf(is(24), is(25), is(26))));
    }

    @Test
    @DisplayName("User can get recommended friends list when he has friends")
    @WithMockUser("27")
    void testGetFriendsRecommendation_whenUserHasFriends_returnsFriendsRecommendationList() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/friends/recommendations");

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(10)));
    }

    @Test
    @DisplayName("User can get friend outgoing request list")
    @WithMockUser("5")
    void testGetFriendsOutgoingRequest_returnsListOfFriendRequests() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/friends/outgoing_requests");

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(6)));
    }
}
