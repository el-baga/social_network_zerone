package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.Friendships;
import com.skillbox.zerone.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface FriendshipsRepository extends JpaRepository<Friendships, Integer>, JpaSpecificationExecutor<Friendships> {

    Optional<Friendships> findFriendshipsBySrcPersonIdAndDstPersonId(Person user, Person friend);

    List<Friendships> findAllBySrcPersonId(Person srcPersonId);

    List<Friendships> findAllByDstPersonId(Person dstPersonId);
}

