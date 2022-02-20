package com.ys.chatserver.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserSearchResponseDto {
    List<UserInfoDto> userInfoDtoList;

    @Builder
    public UserSearchResponseDto(List<UserInfoDto> userInfoDtoList) {
        this.userInfoDtoList = userInfoDtoList;
    }
}
