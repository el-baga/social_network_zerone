package com.skillbox.zerone.service;

import com.skillbox.zerone.dto.response.ComplexRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.dto.response.PersonRs;
import com.skillbox.zerone.entity.FriendshipStatus;
import com.skillbox.zerone.entity.Friendships_;
import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.entity.Person_;
import com.skillbox.zerone.mapper.PersonMapper;
import com.skillbox.zerone.repository.PersonRepository;
import com.skillbox.zerone.specification.Specs;
import com.skillbox.zerone.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FriendsService {
    private final PersonRepository personRepository;
    private final FriendshipsService friendshipsService;
    private final PersonService personService;

    public PageRs<ComplexRs> sendFriendshipRequest(long friendId) {
        friendshipsService.createFriendshipRequest(personService.getPersonById(CommonUtil.getCurrentUserId()), personService.getPersonById(friendId));
        return PageRs.<ComplexRs>builder()
                .data(ComplexRs.builder().build())
                .build();
    }

    public PageRs<ComplexRs> deleteFriendById(long friendId) {
        friendshipsService.deleteFriend(personService.getPersonById(CommonUtil.getCurrentUserId()), personService.getPersonById(friendId));
        return PageRs.<ComplexRs>builder()
                .data(ComplexRs.builder().build())
                .build();
    }

    public PageRs<ComplexRs> addFriendById(long friendId) {
        friendshipsService.addFriend(personService.getPersonById(CommonUtil.getCurrentUserId()), personService.getPersonById(friendId));
        return PageRs.<ComplexRs>builder()
                .data(ComplexRs.builder().build())
                .build();
    }

    public PageRs<ComplexRs> declineFriendshipRequestById(long friendId) {
        friendshipsService.declineFriendshipRequest(personService.getPersonById(CommonUtil.getCurrentUserId()), personService.getPersonById(friendId));
        return PageRs.<ComplexRs>builder()
                .data(ComplexRs.builder().build())
                .build();
    }

    public PageRs<ComplexRs> blockUnblockFriendById(long friendId) {
        friendshipsService.blockUnblockFriend(personService.getPersonById(CommonUtil.getCurrentUserId()), personService.getPersonById(friendId));
        return PageRs.<ComplexRs>builder()
                .data(ComplexRs.builder().build())
                .build();
    }

    public PageRs<List<PersonRs>> getFriendsResponse(int offset, int perPage) {
        List<PersonRs> personRsList = getFriendsList(personService.getPersonById(CommonUtil.getCurrentUserId()))
            .stream()
            .map(person -> PersonMapper.INSTANCE.personToPersonDTO(person, FriendshipStatus.FRIEND.toString(), false))
            .toList();
        return PageRs.<List<PersonRs>>builder()
                .data(personRsList)
                .itemPerPage(perPage)
                .offset(offset)
                .perPage(perPage)
                .total((long) personRsList.size())
                .build();
    }

    public List<Person> getFriendsList(Person user) {
        return personRepository.findAll(Specification
                .where(Specs.eq(Person_.dstFriends, Friendships_.srcPersonId, user))
                .and(Specs.eq(Person_.dstFriends, Friendships_.statusName, FriendshipStatus.FRIEND.toString())));
    }

    public PageRs<List<PersonRs>> getFriendsRequestResponse(int offset, int perPage) {
        List<PersonRs> personRsList = getFriendsRequestList(personService.getPersonById(CommonUtil.getCurrentUserId()))
            .stream()
            .map(person -> PersonMapper.INSTANCE.personToPersonDTO(person, FriendshipStatus.RECEIVED_REQUEST.toString(), false))
                .toList();
        return PageRs.<List<PersonRs>>builder()
                .data(personRsList)
                .itemPerPage(perPage)
                .offset(offset)
                .perPage(perPage)
                .total((long) personRsList.size())
                .build();
    }

    private List<Person> getFriendsRequestList(Person user) {
        return personRepository.findAll(Specification
                .where(Specs.eq(Person_.srcFriends, Friendships_.dstPersonId, user))
                .and(Specs.eq(Person_.srcFriends, Friendships_.statusName, FriendshipStatus.REQUEST.toString())));
    }

    public PageRs<List<PersonRs>> getFriendsRecommendation() {
        List<PersonRs> personRsList = getFriendsRecommendationListByEmail(personService.getPersonById(CommonUtil.getCurrentUserId()))
            .stream()
            .map(person -> PersonMapper.INSTANCE.personToPersonDTO(person, FriendshipStatus.UNKNOWN.toString(), false))
            .toList();
        return PageRs.<List<PersonRs>>builder()
                .data(personRsList)
                .itemPerPage(0)
                .offset(0)
                .perPage(0)
                .total((long) personRsList.size())
                .build();
    }

    private List<Person> getFriendsRecommendationListByEmail(Person user) {
        if (getFriendsList(user).isEmpty()) {
            return getRecommendationListForUserWithoutFriends(user);
        }
        return getRecommendationListForUserHasFriends(user);
    }

    private List<Person> getRecommendationListForUserWithoutFriends(Person user) {
        List<Person> rec = new ArrayList<>();
        List<Person> list = personRepository
                .findAll(Specification
                        .where(Specs.notEq(Person_.id, user.getId()))
                        .and(Specs.between(Person_.birthDate, user.getBirthDate()))
                        .and(Specs.eq(Person_.city,  user.getCity())),
                        PageRequest.of(0, 20,
                        Sort.by(Sort.Direction.DESC, "regDate"))
                ).getContent();
        list.stream()
                .filter(f -> !getFriendsList(user).contains(f))
                .filter(f -> !getFriendsRequestList(user).contains(f))
                .filter(f -> !getFriendsOutgoingRequestList(user).contains(f))
                .filter(f -> !rec.contains(f))
                .forEach(f -> {
                    if (rec.size() == 10) {
                        return;
                    }

                    rec.add(f);
                });
        if (rec.size() < 10) {
            personRepository
                    .findAll(Specification
                            .where(Specs.notEq(Person_.id, user.getId())),
                            PageRequest.of(0, 30,
                            Sort.by(Sort.Direction.DESC, "regDate"))
                    ).getContent().stream()
                    .filter(f -> !getFriendsList(user).contains(f))
                    .filter(f -> !getFriendsRequestList(user).contains(f))
                    .filter(f -> !getFriendsOutgoingRequestList(user).contains(f))
                    .filter(f -> !rec.contains(f))
                    .forEach(f -> {
                        if (rec.size() == 10) {
                            return;
                        }
                        rec.add(f);
                    });
        }
        return rec;
    }

    private List<Person> getRecommendationListForUserHasFriends(Person user) {
        List<Person> rec = new ArrayList<>();
        for (Person friend : getFriendsList(user)) {
            getFriendsList(friend)
                    .stream()
                    .filter(f -> !f.equals(user))
                    .filter(f -> !getFriendsList(user).contains(f))
                    .filter(f -> !rec.contains(f))
                    .forEach(f -> {
                        if (rec.size() == 10) {
                            return;
                        }
                        rec.add(f);
                    });
        }
        if (rec.size() < 10) {
            getRecommendationListForUserWithoutFriends(user)
                    .stream()
                    .filter(f -> !rec.contains(f))
                    .forEach(f -> {
                        if (rec.size() == 10) {
                            return;
                        }
                        rec.add(f);
                    });
        }
        return rec;
    }

    public PageRs<List<PersonRs>> getFriendsOutgoingRequestResponse(int offset, int perPage) {
        List<PersonRs> personRsList = getFriendsOutgoingRequestList(personService.getPersonById(CommonUtil.getCurrentUserId()))
            .stream()
            .map(person -> PersonMapper.INSTANCE.personToPersonDTO(person, FriendshipStatus.REQUEST.toString(), false))
            .toList();
        return PageRs.<List<PersonRs>>builder()
                .data(personRsList)
                .itemPerPage(perPage)
                .offset(offset)
                .perPage(perPage)
                .total((long) personRsList.size())
                .build();
    }

    private List<Person> getFriendsOutgoingRequestList(Person person) {
        return personRepository.findAll(Specification
                .where(Specs.eq(Person_.dstFriends, Friendships_.statusName, FriendshipStatus.REQUEST.toString()))
                .and(Specs.eq(Person_.dstFriends, Friendships_.srcPersonId, person)));
    }
}
