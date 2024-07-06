package com.skillbox.zerone.scheduler;

import com.dropbox.core.DbxException;
import com.skillbox.zerone.entity.*;
import com.skillbox.zerone.exception.BadRequestException;
import com.skillbox.zerone.repository.*;
import com.skillbox.zerone.service.CommentService;
import com.skillbox.zerone.service.MessageService;
import com.skillbox.zerone.service.NotificationService;
import com.skillbox.zerone.service.StorageService;
import com.skillbox.zerone.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(name = "scheduler.enable", matchIfMissing = true)
public class JobScheduler {

    private final PersonRepository personRepository;
    private final CaptchaRepository captchaRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final CommentService commentService;
    private final TagRepository tagRepository;
    private final NotificationService notificationService;
    private final MessageService messageService;
    private final NotificationRepository notificationRepository;
    private final FriendshipsRepository friendshipsRepository;
    private final PersonSettingsRepository personSettingsRepository;
    private final StorageService storageService;
    private final WeatherService weatherService;
    private final CurrencyService currencyService;
    private final PersonService personService;


    @Scheduled(cron = "${scheduler.captcha-clear}")
    public void deleteExpireCaptcha() {
        captchaRepository.deleteAllInBatch(captchaRepository.findByTimeBefore(LocalDateTime.now().minusMinutes(3)));
    }

    @Transactional
    @Scheduled(cron = "${scheduler.post-delete}")
    public void deletePosts() {
        final List<Post> posts = postRepository.findAllByTimeDeleteBefore(LocalDateTime.now().minusMinutes(5));
        posts.forEach(post -> post.getTags().forEach(tag -> {
            if (tag.getPosts().size() == 1) {
                tagRepository.delete(tag);
            }
            tag.getPosts().remove(post);
        }));
        final List<Comment> comments = posts.stream().map(Post::getComments).flatMap(List::stream).toList();
        final List<Long> postAndCommentsIds = new ArrayList<>(posts.stream().map(Post::getId).toList());
        postAndCommentsIds.addAll(comments.stream().map(Comment::getId).toList());
        likeRepository.deleteAll(postAndCommentsIds.stream().map(likeRepository::getLikeByEntityId).flatMap(List::stream).toList());
        final List<Comment> subComments = new ArrayList<>();
        comments.forEach(comment -> subComments.addAll(commentRepository.findAllByParentId(comment.getId())));
        subComments.forEach(comment -> {
            comment.setParentId(null);
            commentRepository.save(comment);
            commentRepository.delete(comment);
        });
        commentRepository.deleteAll(comments);
        postRepository.deleteAll(posts);
    }

    @Transactional
    @Scheduled(cron = "${scheduler.comment-delete}")
    public void deleteComments() {
        final List<Comment> subComments = commentRepository.findAllByIsDeletedAndTimeDeleteBeforeAndParentIdIsNotNull(true, LocalDateTime.now().minusSeconds(20));
        final List<Comment> comments = commentRepository.findAllByIsDeletedAndTimeDeleteBeforeAndParentIdIsNull(true, LocalDateTime.now().minusSeconds(20));
        subComments.forEach(comment -> {
            Comment parentComment = commentService.findById(comment.getParentId());
            parentComment.getSubComments().remove(comment);
            commentRepository.save(parentComment);
        });
        commentRepository.deleteAll(subComments);
        commentRepository.deleteAll(comments);
    }

    @Transactional
    @Scheduled(cron = "${scheduler.birth-day-notify}")
    public void birthDayNotify() {
        notificationService.addBirthDateNotify();
    }

    @Scheduled(cron = "${scheduler.notify-clear}")
    public void notifyClear() {
        notificationService.deleteShownNotification();
    }

    @Scheduled(cron = "${scheduler.messages-clear}")
    public void deleteMessages() {
        messageService.deleteMessages();
    }

    @Scheduled(cron = "${scheduler.get-currency}")
    public void currencyUpdate() {
        currencyService.updateCurrencyData();
    }

    @Scheduled(cron = "${scheduler.get-weather}")
    public void weatherUpdate() {
        Set<String> cities = personService.getCities();
        weatherService.updateWeather(cities);
    }

    @Transactional
    @Scheduled(cron = "${scheduler.account-delete}")
    public void erasureAccountData() {
        final long numberOfDaysForDeletingAcc = 7;
        List<Person> permanentDeletionPersonList = personRepository.findAllByIsDeleted(true).stream()
                .filter(person -> {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    LocalDateTime deletedDateTime = person.getDeletedTime();
                    long days = deletedDateTime.until(currentDateTime, ChronoUnit.DAYS);
                    return days == numberOfDaysForDeletingAcc;
                }).toList();

        deleteAllPersonForeignKeys(permanentDeletionPersonList);
        personRepository.deleteAll(permanentDeletionPersonList);
    }

    private void deleteAllPersonForeignKeys(List<Person> permanentDeletionPersonList) {
        permanentDeletionPersonList.forEach(person -> {
            List<Post> posts = postRepository.findAllByAuthorId(person.getId());
            List<Comment> commentList = commentRepository.findAllByAuthorId(person.getId());
            List<Comment> comments = posts.stream().map(Post::getComments).flatMap(List::stream).filter(comment -> !comment.getAuthor().getId().equals(person.getId())).toList();
            commentList.addAll(comments);

            deleteTags(posts);
            deleteLikes(posts, commentList);
            deleteComments(commentList);
            deletePosts(posts);
            deleteNotifications(person);
            deleteFriendShips(person);
            deletePersonSettings(person);
            deleteStorage(person.getId());
        });
    }

    private void deleteTags(List<Post> posts) {
        posts.forEach(post -> post.getTags().forEach(tag -> {
            if (tag.getPosts().size() == 1) {
                tagRepository.delete(tag);
            }
            tag.getPosts().remove(post);
        }));
    }

    private void deleteLikes(List<Post> posts, List<Comment> commentList) {
        List<Long> postAndCommentsIds = new ArrayList<>(posts.stream().map(Post::getId).toList());
        postAndCommentsIds.addAll(commentList.stream().map(Comment::getId).toList());
        likeRepository.deleteAll(postAndCommentsIds.stream().map(likeRepository::getLikeByEntityId).flatMap(List::stream).toList());
    }

    private void deleteComments(List<Comment> commentList) {
        List<Comment> subComments = new ArrayList<>();
        commentList.forEach(comment -> subComments.addAll(commentRepository.findAllByParentId(comment.getId())));
        subComments.forEach(comment -> {
            comment.setParentId(null);
            commentRepository.save(comment);
            commentRepository.delete(comment);
        });
        commentRepository.deleteAll(commentList);
    }

    private void deletePosts(List<Post> posts) {
        postRepository.deleteAll(posts);
    }

    private void deleteNotifications(Person person) {
        List<Notification> notificationListOfPersonId = notificationRepository.findAllByPersonId(person.getId());
        List<Notification> notificationListOfSenderId = notificationRepository.findAllBySenderId(person.getId());
        notificationRepository.deleteAll(notificationListOfPersonId);
        notificationRepository.deleteAll(notificationListOfSenderId);
    }

    private void deleteFriendShips(Person person) {
        List<Friendships> friendshipsListOfSrcPerson = friendshipsRepository.findAllBySrcPersonId(person);
        List<Friendships> friendshipsListOfDstPerson = friendshipsRepository.findAllByDstPersonId(person);
        friendshipsRepository.deleteAll(friendshipsListOfSrcPerson);
        friendshipsRepository.deleteAll(friendshipsListOfDstPerson);
    }

    private void deletePersonSettings(Person person) {
        PersonSettings personSettings = person.getPersonSettings();
        personSettingsRepository.delete(personSettings);
    }

    private void deleteStorage(Long personId) {
        try {
            storageService.deleteImage(personId);
        } catch (DbxException e) {
            throw new BadRequestException("Something went wrong with image processing.");
        }
    }
}
