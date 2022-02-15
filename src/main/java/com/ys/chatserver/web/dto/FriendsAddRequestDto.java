package com.ys.chatserver.web.dto;

import com.ys.chatserver.domain.friends.Friends;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendsAddRequestDto {
    private Long userId;
    private Long friendId;

    @Builder
    public FriendsAddRequestDto(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public Friends toEntity() {
        return Friends.builder()
                .id(friendId)
                .build();
    }
}
