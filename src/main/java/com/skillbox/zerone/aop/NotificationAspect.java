package com.skillbox.zerone.aop;

import com.skillbox.zerone.dto.request.LikeRq;
import com.skillbox.zerone.dto.response.CommentRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.dto.response.PostRs;
import com.skillbox.zerone.entity.Person;
import com.skillbox.zerone.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class NotificationAspect {
    private final NotificationService notificationService;

    @AfterReturning(pointcut = "execution(* com.skillbox.zerone.service.FriendshipsService.createFriendshipRequest(..))")
    public void afterFriendshipRequest(JoinPoint jp) {
        final var args = jp.getArgs();
        notificationService.friendRequestNotify((Person) args[0], (Person) args[1]);
    }

    @AfterReturning(pointcut = "execution(* com.skillbox.zerone.service.CommentService.addComment(..))", returning = "page")
    public void afterAddComment(PageRs<CommentRs> page) {
        notificationService.addCommentNotify(page.getData().getId());
    }

    @AfterReturning(pointcut = "execution(* com.skillbox.zerone.service.LikeService.addLike(..))")
    public void afterAddLike(JoinPoint jp) {
        final var args = jp.getArgs();
        notificationService.addLikeNotify((LikeRq) args[0]);
    }

    @AfterReturning(pointcut = "execution(* com.skillbox.zerone.service.PostService.createNewPost(..))", returning = "post")
    public void afterCreateNewPost(JoinPoint jp, PageRs<PostRs> post) {
        final var args = jp.getArgs();
        if (args[2] == null) {
            notificationService.addCreateNewPostNotify((Long) args[0], post.getData().getId());
        }
    }
}
