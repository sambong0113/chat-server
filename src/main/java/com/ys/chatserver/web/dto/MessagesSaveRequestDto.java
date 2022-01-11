package com.ys.chatserver.web.dto;

import com.ys.chatserver.domain.messages.Messages;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessagesSaveRequestDto {
    private String content;
    private String author;

    @Builder
    public MessagesSaveRequestDto(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public Messages toEntity() {
        return Messages.builder()
                .content(content)
                .author(author)
                .build();
    }
}
