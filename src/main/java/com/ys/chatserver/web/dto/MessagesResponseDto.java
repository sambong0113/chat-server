package com.ys.chatserver.web.dto;

import com.ys.chatserver.domain.messages.Messages;
import lombok.Getter;

@Getter
public class MessagesResponseDto {

    private Long id;
    private String content;
    private String author;

    public MessagesResponseDto(Messages entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
