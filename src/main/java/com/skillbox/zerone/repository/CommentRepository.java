package com.skillbox.zerone.repository;

import com.skillbox.zerone.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    List<Comment> findAllByIsDeletedAndTimeDeleteBeforeAndParentIdIsNull(Boolean isDeleted, LocalDateTime time);
    List<Comment> findAllByIsDeletedAndTimeDeleteBeforeAndParentIdIsNotNull(Boolean isDeleted, LocalDateTime time);
    List<Comment> findAllByParentId(Long parentId);
    List<Comment> findAllByAuthorId(Long authorId);
    Optional<Comment> findByIdAndPostId(Long id, Long postId);
}
