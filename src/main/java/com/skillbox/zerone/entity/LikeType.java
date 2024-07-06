package com.skillbox.zerone.entity;

import lombok.Getter;

@Getter
public enum LikeType {
    POST("Post"),
    COMMENT("Comment");

    private final String typeName;

    LikeType(String typeName) {
        this.typeName = typeName;
    }
}
