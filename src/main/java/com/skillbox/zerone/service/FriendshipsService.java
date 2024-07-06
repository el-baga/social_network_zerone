package com.skillbox.zerone.service;


import com.skillbox.zerone.entity.Friendships_;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.entity.FriendshipStatus;
import com.skillbox.zerone.entity.Friendships;
import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.repository.FriendshipsRepository;
import com.skillbox.zerone.specification.Specs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipsService {
    private final FriendshipsRepository friendshipsRepository;

    @Transactional
    public void createFriendshipRequest(Person user, Person friend) {
        friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(user, friend).ifPresent(s -> {
            throw new BadRequestException("Friendship already used");
        });

        friendshipsRepository.save(Friendships.builder()
                .sentTime(LocalDateTime.now())
                .srcPersonId(user)
                .dstPersonId(friend)
                .statusName(FriendshipStatus.REQUEST.toString())
                .build()
        );
        friendshipsRepository.save(Friendships.builder()
                .sentTime(LocalDateTime.now())
                .srcPersonId(friend)
                .dstPersonId(user)
                .statusName(FriendshipStatus.RECEIVED_REQUEST.toString())
                .build()
        );
    }

    @Transactional
    public void deleteFriend(Person user, Person friend) {
        List<Friendships> friendshipsList = friendshipsRepository.findAll(Specification
                .where(Specs.eq(Friendships_.srcPersonId, user).or(Specs.eq(Friendships_.srcPersonId, friend)))
                .and(Specs.eq(Friendships_.dstPersonId, user).or(Specs.eq(Friendships_.dstPersonId, friend)))
                .and(Specs.eq(Friendships_.statusName, FriendshipStatus.FRIEND.toString())));
        if (friendshipsList.isEmpty()) {
            throw new BadRequestException("Friendship not found");
        }
        friendshipsRepository.deleteAll(friendshipsList);
    }

    @Transactional
    public void addFriend(Person user, Person friend) {
        Optional<Friendships> friendship = getFriendships(user, friend, FriendshipStatus.RECEIVED_REQUEST.toString());
        Optional<Friendships> friendshipRq = getFriendships(friend, user, FriendshipStatus.REQUEST.toString());
        if (friendship.isPresent() &&  friendshipRq.isPresent()) {
            friendship.get().setStatusName(String.valueOf(FriendshipStatus.FRIEND));
            friendshipRq.get().setStatusName(String.valueOf(FriendshipStatus.FRIEND));
            friendshipsRepository.save(friendship.get());
            friendshipsRepository.save(friendshipRq.get());
        }
    }

    public Optional<Friendships> getFriendships(Person person1, Person person2, String friendshipStatus) {
        return friendshipsRepository.findOne(Specification
                .where(Specs.eq(Friendships_.srcPersonId, person1))
                .and(Specs.eq(Friendships_.dstPersonId, person2))
                .and(Specs.eq(Friendships_.statusName,friendshipStatus)));
    }

    @Transactional
    public void declineFriendshipRequest(Person user, Person friend) {
        Optional<Friendships> friendship = getFriendships(user, friend, FriendshipStatus.RECEIVED_REQUEST.toString());
        Optional<Friendships> friendshipRq = getFriendships(friend, user, FriendshipStatus.REQUEST.toString());
        Optional<Friendships> friendshipOutgoing = getFriendships(friend, user, FriendshipStatus.RECEIVED_REQUEST.toString());
        Optional<Friendships> friendshipRqOutgoing = getFriendships(user, friend,  FriendshipStatus.REQUEST.toString());
        if (friendship.isPresent() &&  friendshipRq.isPresent()) {
            friendshipsRepository.delete(friendship.get());
            friendshipsRepository.delete(friendshipRq.get());
        }
        else if (friendshipOutgoing.isPresent() &&  friendshipRqOutgoing.isPresent()) {
            friendshipsRepository.delete(friendshipOutgoing.get());
            friendshipsRepository.delete(friendshipRqOutgoing.get());
        }
    }

    //шаблон метода
    public void blockUnblockFriend(Person user, Person friend) {
        Friendships friendship = friendshipsRepository.findFriendshipsBySrcPersonIdAndDstPersonId(user, friend).orElseThrow(() ->
                new BadRequestException("Friendship not found"));
        if (friendship.getStatusName().equals(FriendshipStatus.BLOCKED.toString())) {
            friendship.setStatusName(FriendshipStatus.FRIEND.toString());
        } else {
            friendship.setStatusName(FriendshipStatus.BLOCKED.toString());
        }
        friendshipsRepository.save(friendship);
    }
}
