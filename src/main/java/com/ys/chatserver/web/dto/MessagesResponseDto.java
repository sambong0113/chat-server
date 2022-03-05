package com.ys.chatserver.web.dto;

import com.ys.chatserver.domain.messages.Message;
import lombok.Getter;

@Getter
public class MessagesResponseDto {

    private Long id;
    private Long userId;
    private Long chatRoomId;

    public MessagesResponseDto(Message entity) {
        this.id = entity.getId();
        this.userId = entity.getUser().getUserSeq();
        this.chatRoomId = entity.getChatRoom().getId();
    }
}
