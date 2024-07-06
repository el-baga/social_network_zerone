package com.skillbox.zerone.service;

import com.skillbox.zerone.dto.request.CommentRq;
import com.skillbox.zerone.dto.response.CommentRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.entity.Comment;
import com.skillbox.zerone.entity.Comment_;
import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.entity.Post;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.mapper.CommentMapper;
import com.skillbox.zerone.mapper.PostMapper;
import com.skillbox.zerone.repository.CommentRepository;
import com.skillbox.zerone.repository.PostRepository;
import com.skillbox.zerone.specification.Specs;
import com.skillbox.zerone.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final PersonService personService;

    public PageRs<CommentRs> addComment(Long postId, CommentRq commentRq) {
        if (commentRq.getCommentText().isBlank()) {
            throw new BadRequestException("Comment can not be empty or blank");
        }
        final Post post = postService.findById(postId);
        checkParentCommentByIdAndPostId(commentRq.getParentId(), postId);
        commentRq.setIsDeleted(false);
        final Person author = personService.getPersonById(CommonUtil.getCurrentUserId());
        final Comment comment = CommentMapper.INSTANCE.toEntity(commentRq, postId, author, post, new ArrayList<>());
        post.getComments().add(comment);
        postRepository.save(post);
        return PageRs.<CommentRs>builder()
                .data(PostMapper.INSTANCE.toCommentDto(comment, CommonUtil.getCurrentUserId()))
                .build();
    }

    public PageRs<CommentRs> softDelete(Long commentId) {
        Comment comment = findById(commentId);
        if (!comment.getAuthor().getId().equals(CommonUtil.getCurrentUserId())) {
            throw new BadRequestException("You can delete only your comments");
        }
        comment.setIsDeleted(true);
        comment.setTimeDelete(LocalDateTime.now());
        comment = commentRepository.save(comment);
        return PageRs.<CommentRs>builder()
                .data(PostMapper.INSTANCE.toCommentDto(comment, CommonUtil.getCurrentUserId()))
                .build();
    }

    public PageRs<CommentRs> recover(Long commentId) {
        Comment comment = findById(commentId);
        if (!comment.getAuthor().getId().equals(CommonUtil.getCurrentUserId())) {
            throw new BadRequestException("You can recover only your comments");
        }
        comment.setIsDeleted(false);
        comment.setTimeDelete(null);
        comment = commentRepository.save(comment);
        return PageRs.<CommentRs>builder()
                .data(PostMapper.INSTANCE.toCommentDto(comment, CommonUtil.getCurrentUserId()))
                .build();
    }

    public PageRs<List<CommentRs>> getComments(Long postId, Integer offset, Integer perPage) {
        final Page<Comment> commentPage = commentRepository.findAll(
                Specification.where(Specs.eq(Comment_.isBlocked, false))
                        .and(Specs.isNull(Comment_.parentId))
                        .and(Specs.eq(Comment_.postId, postId)),
                PageRequest.of(
                        CommonUtil.offsetToPageNum(offset, perPage),
                        perPage,
                        Sort.by(Sort.Order.asc("time"))
                )
        );
        final List<CommentRs> commentRsList = new ArrayList<>();
        commentPage.getContent().forEach(comment -> {
            CommentRs commentRs = PostMapper.INSTANCE.toCommentDto(comment, CommonUtil.getCurrentUserId());
            commentRsList.add(commentRs);
        });
        return PageRs.<List<CommentRs>>builder()
                .data(commentRsList)
                .itemPerPage(perPage)
                .offset(offset)
                .perPage(perPage)
                .total(commentPage.getTotalElements())
                .build();
    }

    public PageRs<CommentRs> updateComment(Long commentId, CommentRq commentRq) {
        if (commentRq.getCommentText().isBlank()) {
            throw new BadRequestException("Comment can not be empty or blank");
        }
        Comment comment = findById(commentId);
        if (!comment.getAuthor().getId().equals(CommonUtil.getCurrentUserId())) {
            throw new BadRequestException("You can edit only your own comments");
        }
        comment.setCommentText(commentRq.getCommentText());
        comment = commentRepository.save(comment);

        return PageRs.<CommentRs>builder()
                .data(PostMapper.INSTANCE.toCommentDto(comment, CommonUtil.getCurrentUserId()))
                .build();
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new BadRequestException("Comment with id: " + commentId + " not found"));
    }

    public void checkParentCommentByIdAndPostId(Long parentCommentId, Long postId) {
        if(parentCommentId == null) return;
        commentRepository.findByIdAndPostId(parentCommentId, postId).orElseThrow(
                () -> new BadRequestException("Comment with id: " + parentCommentId + " in post with id: " + postId + " not found"));
    }
}
