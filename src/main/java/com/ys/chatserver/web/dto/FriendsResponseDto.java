package com.ys.chatserver.web.dto;

import com.ys.chatserver.domain.friends.Friends;
import com.ys.chatserver.domain.user.User;
import lombok.Getter;

import java.util.Set;

@Getter
public class FriendsResponseDto {
    private Long id;
    private Set<User> friendSet;

    public FriendsResponseDto(Friends entity) {
        this.id = entity.getId();
        this.friendSet = entity.getFriendSet();
    }
}
