package com.ys.chatserver.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="UER_REFRESH_TOKEN")
public class UserRefreshToken {

    @Id
    @Column(name = "REFRESH_TOKEN_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenSeq;

    @Column(name = "USER_ID", length = 64, unique = true)
    private String userId;


    @Column(name = "REFRESH_TOKEN", length = 256)
    private String refreshToken;

    public UserRefreshToken(String userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
