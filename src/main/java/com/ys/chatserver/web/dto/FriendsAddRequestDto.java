package com.ys.chatserver.web.dto;

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
}
