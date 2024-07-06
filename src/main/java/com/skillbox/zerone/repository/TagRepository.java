package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag,Long>{

    Optional<Tag> findByTag(String tagString);

    @Query(value = "SELECT t.* FROM tags t JOIN post2tag p2t ON t.id = p2t.tag_id JOIN posts p ON p.id = p2t.post_id WHERE p.id = :postId", nativeQuery = true)
    List<Tag> findByPostId(Long postId);
}
