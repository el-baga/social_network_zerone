package com.skillbox.zerone.service;

import com.skillbox.zerone.aop.LoggableDebug;
import com.skillbox.zerone.dto.request.*;
import com.skillbox.zerone.dto.response.NotificationRs;
import com.skillbox.zerone.dto.response.NotificationSettingsRs;
import com.skillbox.zerone.dto.response.PageRs;
import com.skillbox.zerone.entity.*;
import com.skillbox.zerone.listener.KafkaMessage;
import com.skillbox.zerone.mapper.NotifyMapper;
import com.skillbox.zerone.repository.NotificationRepository;
import com.skillbox.zerone.specification.Specs;
import com.skillbox.zerone.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@LoggableDebug
@RequiredArgsConstructor
public class NotificationService {
    @Value("${app.kafka.kafkaMessageTopicBot}")
    private String topicNameBot;

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;
    private final NotificationRepository notificationRepository;
    private final PersonService personService;
    private final SimpMessageSendingOperations messagingTemplate;
    private final CommentService commentService;
    private final FriendsService friendsService;
    private final PostService postService;

    public PageRs<List<NotificationSettingsRs>> getSettings() {
        final var settings = personService.getPersonById(CommonUtil.getCurrentUserId()).getPersonSettings();
        final var list = new ArrayList<NotificationSettingsRs>();
        list.add(NotifyMapper.INSTANCE.toSettingDto(NotificationType.COMMENT_COMMENT, settings.getCommentComment()));
        list.add(NotifyMapper.INSTANCE.toSettingDto(NotificationType.FRIEND_BIRTHDAY, settings.getFriendBirthday()));
        list.add(NotifyMapper.INSTANCE.toSettingDto(NotificationType.FRIEND_REQUEST, settings.getFriendRequest()));
        list.add(NotifyMapper.INSTANCE.toSettingDto(NotificationType.MESSAGE, settings.getMessage()));
        list.add(NotifyMapper.INSTANCE.toSettingDto(NotificationType.POST, settings.getPost()));
        list.add(NotifyMapper.INSTANCE.toSettingDto(NotificationType.POST_COMMENT, settings.getPostComment()));
        list.add(NotifyMapper.INSTANCE.toSettingDto(NotificationType.POST_LIKE, settings.getPostLike()));

        return PageRs.<List<NotificationSettingsRs>>builder().data(list).build();
    }

    public PageRs<String> setSetting(NotificationSettingRq req) {
        final var person = personService.getPersonById(CommonUtil.getCurrentUserId());

        switch (req.getNotificationType()) {
            case COMMENT_COMMENT -> person.getPersonSettings().setCommentComment(req.getEnable());
            case FRIEND_BIRTHDAY -> person.getPersonSettings().setFriendBirthday(req.getEnable());
            case FRIEND_REQUEST -> person.getPersonSettings().setFriendRequest(req.getEnable());
            case MESSAGE -> person.getPersonSettings().setMessage(req.getEnable());
            case POST -> person.getPersonSettings().setPost(req.getEnable());
            case POST_COMMENT -> person.getPersonSettings().setPostComment(req.getEnable());
            case POST_LIKE, COMMENT_LIKE -> person.getPersonSettings().setPostLike(req.getEnable());
        }
        personService.updatePerson(person);

        return PageRs.<String>builder().data("success").build();
    }

    public PageRs<List<NotificationRs>> getNotifications(NotificationsRq req) {
        final var page = getNotificationsPage(req);
        return PageRs
            .<List<NotificationRs>>builder()
            .data(page.getContent().stream().map(NotifyMapper.INSTANCE::toDto).toList())
            .itemPerPage(page.getNumberOfElements())
            .offset(req.getOffset())
            .perPage(req.getItemPerPage())
            .total(page.getTotalElements())
            .build();
    }

    private Page<Notification> getNotificationsPage(NotificationsRq req) {
        return notificationRepository.findAll(
            Specification.where(Specs.eq(Notification_.person, personService.getPersonById(CommonUtil.getCurrentUserId())))
                .and(Specs.eq(Notification_.isRead, req.getIsRead())),
            PageRequest.of(
                CommonUtil.offsetToPageNum(req.getOffset(), req.getItemPerPage()),
                req.getItemPerPage(),
                Sort.by(Sort.Direction.ASC, Notification_.SENT_TIME)
            )
        );
    }

    public PageRs<String> setNotificationRead(NotificationsSetRq req) {
        if (req.getAll().equals(true)) {
            final var list = getNotificationsPage(new NotificationsRq()).getContent();
            list.forEach(notification -> notification.setIsRead(true));
            notificationRepository.saveAll(list);
        }
        else if (req.getId() > 0) {
            notificationRepository.findById(req.getId()).ifPresent(notification -> {
                notification.setIsRead(true);
                notificationRepository.save(notification);
            });
        }
        return PageRs.<String>builder().data("success").build();
    }

    public void friendRequestNotify(Person user, Person friend) {
        if (friend.getPersonSettings().getFriendRequest().equals(true)){
            sendNotification(
                user,
                friend,
                user.getId(),
                String.format("%s %s отправил(а) вам заявку в друзья", user.getFirstName(), user.getLastName()),
                NotificationType.FRIEND_REQUEST
            );
        }
    }

    public void addCreateNewPostNotify(Long postSenderId, Long postId) {
        final var sender = personService.getPersonById(postSenderId);
        final var info = String.format("%s %s опубликовал(а) новый пост", sender.getFirstName(), sender.getLastName());

        friendsService.getFriendsList(sender).forEach(friend -> {
            if (friend.getPersonSettings().getPost().equals(true)){
                sendNotification(sender, friend, postId, info, NotificationType.POST);
            }
        });
    }

    public void addCommentNotify(Long commentId) {
        final var comment = commentService.findById(commentId);
        final var sender = comment.getAuthor();
        var info = String.format("%s %s ", sender.getFirstName(), sender.getLastName());
        var receiver = comment.getPost().getAuthor();
        var entityId = comment.getParentId();
        var type = NotificationType.POST_COMMENT;
        var isSendEnable = receiver.getPersonSettings().getPostComment().equals(true);

        if (entityId == null) {
            info += "прокомментировал(а) ваш пост";
            entityId = comment.getPostId();
        }
        else {
            info += "ответил(а) на ваш комментарий";
            receiver = commentService.findById(entityId).getAuthor();
            type = NotificationType.COMMENT_COMMENT;
            isSendEnable = receiver.getPersonSettings().getCommentComment().equals(true);
        }

        if (isSendEnable) {
            sendNotification(sender, receiver, entityId, info, type);
        }
    }

    public void addLikeNotify(LikeRq like) {
        final var sender = personService.getPersonById(CommonUtil.getCurrentUserId());
        var info = String.format("%s %s ", sender.getFirstName(), sender.getLastName());
        var receiver = like.getType().equals("Post") ?
                postService.findById(like.getItemId()).getAuthor() : postService.findById(like.getPostId()).getAuthor();
        var type = NotificationType.POST_LIKE;

        if (like.getType().equals("Comment")) {
            info += "поставил(а) лайк вашему комментарию";
            receiver = commentService.findById(like.getItemId()).getAuthor();
            type = NotificationType.COMMENT_LIKE;
        }
        else {
            info += "поставил(а) лайк вашему посту";
        }

        if (receiver.getPersonSettings().getPostLike().equals(true)) {
            sendNotification(sender, receiver, like.getItemId(), info, type);
        }
    }

    public void addBirthDateNotify() {
        personService.getPersonsByBirthDate(LocalDate.now())
            .forEach(sender -> friendsService.getFriendsList(sender)
                .stream()
                .filter(receiver -> receiver.getPersonSettings().getFriendBirthday().equals(true))
                .forEach(receiver -> sendNotification(
                    sender,
                    receiver,
                    sender.getId(),
                    String.format("%s %s отмечает сегодня день рождения", sender.getFirstName(), sender.getLastName()),
                    NotificationType.FRIEND_BIRTHDAY)
                )
            );
    }

    public void sendNotification(Person sender, Person receiver, Long entityId, String info, NotificationType type) {
        if (!sender.equals(receiver)) {
            final var notification = Notification
                .builder()
                .contact(info)
                .entityId(entityId)
                .notificationType(type)
                .person(receiver)
                .sender(sender)
                .build();
            notificationRepository.save(notification);

            messagingTemplate.convertAndSendToUser(
                receiver.getId().toString(),
                "/queue/notifications",
                NotifyMapper.INSTANCE.toDto(notification)
            );
        }
        if (!Objects.isNull(receiver.getTelegramId())) {
        kafkaTemplate.send(topicNameBot, KafkaMessage.builder().message(info)
                .userId(receiver.getTelegramId()).build());
        }
    }

    public void deleteShownNotification(){
        notificationRepository.deleteAllInBatch(
            notificationRepository.findAll(Specification.where(Specs.eq(Notification_.isRead, true)))
        );
    }
}
