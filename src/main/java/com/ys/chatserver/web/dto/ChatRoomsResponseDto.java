package com.ys.chatserver.web.dto;

import com.ys.chatserver.domain.chatRooms.ChatRoom;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.domain.user.dto.UserInfoDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ChatRoomsResponseDto {

    private Long id;
    private String title;
    private LocalDateTime modifiedDate;

    private List<UserInfoDto> userList;

    public ChatRoomsResponseDto(ChatRoom entity, List<User> userList) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.modifiedDate = entity.getModifiedDate();
        this.userList = userList.stream().map(UserInfoDto::new).collect(Collectors.toList());

    }
}
