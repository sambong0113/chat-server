package com.ys.chatserver.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import com.ys.chatserver.auth.dto.ProviderType;
import com.ys.chatserver.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @JsonIgnore
    @Id
    @Column(name = "USER_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(name = "USER_ID", length = 64, unique = true)
    @NotNull
    private String userId;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @Column(name = "PASSWORD", length = 128)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(name = "EMAIL_VERIFIED_YN", length = 1)
    @NotNull
    private String emailVerifiedYn;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Builder
    public User(String userId, String name, String password, String email, String emailVerifiedYn, String picture, ProviderType providerType, Role role) {
        this.userId = userId;
        this.name = name;
        this.password = "NO_PASS";
        this.email = email != null ? email : "NO_EMAIL";
        this.emailVerifiedYn = emailVerifiedYn;
        this.picture = picture != null ? picture : "";
        this.providerType = providerType;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
