package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.Like;
import com.skillbox.zerone.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long>, JpaSpecificationExecutor<Like> {

    Optional<Like> getLikeByEntityIdAndPerson(Long entityId, Person person);

    List<Like> getLikeByEntityId(Long entityId);
}
