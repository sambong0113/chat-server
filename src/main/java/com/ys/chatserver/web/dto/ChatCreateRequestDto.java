package com.ys.chatserver.web.dto;

import lombok.Getter;

@Getter
public class ChatCreateRequestDto {

    private String title;
    private Long userId;

    public ChatCreateRequestDto(String title, Long userId) {
        this.title = title;
        this.userId = userId;
    }
}
