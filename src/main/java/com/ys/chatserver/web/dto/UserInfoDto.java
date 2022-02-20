package com.ys.chatserver.web.dto;

import com.ys.chatserver.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoDto {

    private Long userSeq;
    private String name;
    private String email;
    private String picture;

    @Builder
    public UserInfoDto(User user) {
        this.userSeq = user.getUserSeq();
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getEmail();
    }
}
