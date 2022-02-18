package com.ys.chatserver.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendsAddRequestDto {
    private Long friendId;

    @Builder
    public FriendsAddRequestDto(Long friendId) {
        this.friendId = friendId;
    }
}
