package com.skillbox.zerone.service;


import com.skillbox.zerone.dto.request.LikeRq;
import com.skillbox.zerone.dto.response.LikeRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.entity.Like;
import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.repository.LikeRepository;
import com.skillbox.zerone.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PersonService personService;
    private final PostService postService;
    private final CommentService commentService;

    public PageRs<LikeRs> addLike(LikeRq likeRq) {
        if (likeRq.getType().equals("Comment") && likeRq.getPostId() == null) {
            throw new BadRequestException("Comment must have post id");
        }
        final Person person = personService.getPersonById(CommonUtil.getCurrentUserId());
        if (checkIfLikeExist(likeRq.getItemId(), person) != null) {
            throw new BadRequestException("Ранее вы уже поставили свой лайк");
        }
        final Like like = Like.builder()
                .type(likeRq.getType())
                .entityId(likeRq.getItemId())
                .person(person)
                .time(LocalDateTime.now())
                .post(likeRq.getType().equals("Post") ? postService.findById(likeRq.getItemId()) : postService.findById(likeRq.getPostId()))
                .comment(likeRq.getType().equals("Comment") ? commentService.findById(likeRq.getItemId()) : null)
                .build();
        likeRepository.save(like);
        return getLikeRsPageRs(likeRq.getItemId());
    }

    public PageRs<LikeRs> removeLike(LikeRq likeRq, Long itemId) {
        final Like like = getLikeByEntityIdAndPerson(itemId, personService.getPersonById(CommonUtil.getCurrentUserId()));
        likeRepository.delete(like);
        return getLikeRsPageRs(likeRq.getItemId());
    }

    public PageRs<LikeRs> getLikeRsPageRs(Long itemId) {
        final List<Like> likes = likeRepository.getLikeByEntityId(itemId);
        final LikeRs likeRs = LikeRs.builder()
                .likes((long) likes.size())
                .users(likes.stream().map(Like::getPerson).map(Person::getId).toList())
                .build();
        return PageRs.<LikeRs>builder()
                .data(likeRs)
                .build();
    }

    public Like getLikeByEntityIdAndPerson(Long entityId, Person person) {
        return likeRepository.getLikeByEntityIdAndPerson(entityId, person).orElseThrow(() -> new BadRequestException("Like with entity id: " + entityId + " and user id: " + person.getId() + " is not found"));
    }

    private Like checkIfLikeExist(Long entityId, Person person) {
        return likeRepository.getLikeByEntityIdAndPerson(entityId, person).orElse(null);
    }
    
}
