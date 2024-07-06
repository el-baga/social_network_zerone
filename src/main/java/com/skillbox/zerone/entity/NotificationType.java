package com.skillbox.zerone.entity;

import lombok.Getter;

@Getter
public enum NotificationType {
    COMMENT_COMMENT("Ответы на комментарии"),
    FRIEND_BIRTHDAY("Дни рождения у друзей"),
    FRIEND_REQUEST("Запросы в друзья"),
    MESSAGE("Личные сообщения"),
    POST("Публикации постов"),
    POST_COMMENT("Комментарии к посту"),
    POST_LIKE("Лайки к посту"),
    COMMENT_LIKE("Лайк к комментарию");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }
}
