package com.skillbox.zerone.service;

import com.skillbox.zerone.dto.request.PostRq;
import com.skillbox.zerone.dto.request.PostSearchRq;
import com.skillbox.zerone.dto.request.PostUpdateRq;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.dto.response.PostRs;
import com.skillbox.zerone.entity.*;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.mapper.PostMapper;
import com.skillbox.zerone.repository.PostRepository;
import com.skillbox.zerone.specification.Specs;
import com.skillbox.zerone.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PersonService personService;
    private final TagService tagService;

    public PageRs<List<PostRs>> getFeeds(Integer offset, Integer perPage) {
        final Page<Post> postPage = postRepository.findAll(
                Specification.where(Specs.eq(Post_.isBlocked, false))
                        .and(Specs.eq(Post_.isDeleted, false))
                        .and(Specs.lessEq(Post_.time, LocalDateTime.now())),
                PageRequest.of(
                        CommonUtil.offsetToPageNum(offset, perPage),
                        perPage,
                        Sort.by(Sort.Order.desc("time"), Sort.Order.asc("author.lastName"))
                )
        );
        return PageRs.<List<PostRs>>builder()
                .data(postPage.getContent().stream().map(post -> PostMapper.INSTANCE.toDto(post, CommonUtil.getCurrentUserId())).toList())
                .itemPerPage(perPage)
                .offset(offset)
                .perPage(perPage)
                .total(postPage.getTotalElements())
                .build();
    }

    public PageRs<List<PostRs>> getPersonPosts(Integer offset, Integer perPage, Long userId) {
        final Page<Post> postPage = postRepository.findAll(
                Specification.where(Specs.eq(Post_.isBlocked, false))
                        .and(Specs.eq(Post_.author, Person_.id, userId)),
                PageRequest.of(
                        CommonUtil.offsetToPageNum(offset, perPage),
                        perPage,
                        Sort.by(Sort.Order.desc("time"), Sort.Order.asc("author.lastName"))
                )
        );

        boolean isAccDeleted = personService.getPersonById(userId).getIsDeleted();
        return isAccDeleted ?
                PageRs.<List<PostRs>>builder()
                        .data(new ArrayList<>())
                        .build() :
                PageRs.<List<PostRs>>builder()
                .data(postPage.getContent().stream().map(post -> PostMapper.INSTANCE.toDto(post, CommonUtil.getCurrentUserId())).toList())
                .itemPerPage(perPage)
                .offset(offset)
                .perPage(perPage)
                .total(postPage.getTotalElements())
                .build();
    }

    public PageRs<PostRs> createNewPost(Long authorId, PostRq postRq, Long publishDate) {
        if (!CommonUtil.getCurrentUserId().equals(authorId)) {
            throw new BadRequestException("incorrect author id in path");
        }
        if (publishDate != null && Instant.now().isAfter(Instant.ofEpochMilli(publishDate).plus(1, ChronoUnit.SECONDS))) {
            throw new BadRequestException("Publish date must be after current time");
        }
        postRq.setIsDeleted(false);
        postRq.setTimeDelete(null);
        final Person person = personService.getPersonById(authorId);
        final Post post = PostMapper.INSTANCE.toEntity(postRq);
        if (publishDate != null) {
            post.setTime(Instant.ofEpochMilli(publishDate).atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        post.setTags(tagService.saveAll(post.getTags()));
        post.setAuthor(person);
        postRepository.save(post);
        return PageRs.<PostRs>builder()
                .data(PostMapper.INSTANCE.toDto(post, CommonUtil.getCurrentUserId()))
                .build();
    }

    public PageRs<PostRs> updatePost(Long postId, PostUpdateRq postUpdateRq) {
        if (postUpdateRq.getPostText().isEmpty() || postUpdateRq.getPostText().isBlank()) {
            throw new BadRequestException("Post text can not be empty or blank");
        }
        if (postUpdateRq.getTitle().isEmpty() || postUpdateRq.getTitle().isBlank()) {
            throw new BadRequestException("Post title can not be empty or blank");
        }
        final Post post = findById(postId);
        if (!post.getAuthor().getId().equals(CommonUtil.getCurrentUserId())) {
            throw new BadRequestException("You can edit only your own posts");
        }
        tagService.unlinkUnusedTagsFromPost(post.getTags(), postUpdateRq.getTags(), post);
        post.setPostText(postUpdateRq.getPostText());
        post.setTitle(postUpdateRq.getTitle());
        post.setTags(tagService.saveAll(postUpdateRq.getTags(), post));
        return PageRs.<PostRs>builder()
                .data(PostMapper.INSTANCE.toDto(postRepository.save(post), CommonUtil.getCurrentUserId()))
                .build();
    }

    public PageRs<PostRs> softDeleteOrRecover(Long postId, boolean isDeleted) {
        Post post = findById(postId);
        if (!post.getAuthor().getId().equals(CommonUtil.getCurrentUserId())) {
            throw new BadRequestException("You can delete or recover only your own posts");
        }
        if (post.getIsDeleted().equals(isDeleted)) {
            throw new BadRequestException("Post already have this status. isDeleted:" + isDeleted);
        }
        post.setIsDeleted(isDeleted);
        post.setTimeDelete(isDeleted ? LocalDateTime.now() : null);
        post = postRepository.save(post);
        return PageRs.<PostRs>builder()
                .data(PostMapper.INSTANCE.toDto(post, CommonUtil.getCurrentUserId()))
                .build();
    }

    public PageRs<PostRs> getPost(Long postId) {
        return PageRs.<PostRs>builder()
                .data(PostMapper.INSTANCE.toDto(findById(postId), CommonUtil.getCurrentUserId()))
                .build();
    }

    public Post findById(Long postId) {
        final Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new BadRequestException("There is no post with id: " + postId);
        }
        return postOptional.get();
    }

    public PageRs<List<PostRs>> searchPosts(PostSearchRq req) {
        final Page<Post> page = postRepository.findAll(
                Specification.where(Specs.eq(Post_.isBlocked, false))
                        .and(Specs.eq(Post_.isDeleted, false))
                        .and(Specs.lessEq(Post_.time, LocalDateTime.now()))
                        .and(Specs.joinOr(Post_.author, Map.of(Person_.firstName, req.getAuthor(), Person_.lastName, req.getAuthor())))
                        .and(Specs.like(Post_.title, req.getText()).or(Specs.like(Post_.postText, req.getText())))
                        .and(Specs.in(Post_.tags, Tag_.tag, tagService.replaceStringTag(req.getTags())))
                        .and(Specs.greaterEq(Post_.time, req.getDateFrom()))
                        .and(Specs.lessEq(Post_.time, req.getDateTo())),
                PageRequest.of(
                        CommonUtil.offsetToPageNum(req.getOffset(), req.getPerPage()),
                        req.getPerPage(),
                        Sort.by(Sort.Order.desc(Post_.TIME), Sort.Order.asc(Post_.AUTHOR + "." + Person_.LAST_NAME))
                )
        );

        return PageRs
                .<List<PostRs>>builder()
                .data(page.getContent().stream().map(post -> PostMapper.INSTANCE.toDto(post, CommonUtil.getCurrentUserId())).toList())
                .itemPerPage(page.getNumberOfElements())
                .offset(req.getOffset())
                .perPage(req.getPerPage())
                .total(page.getTotalElements())
                .build();
    }
}
