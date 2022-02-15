package com.ys.chatserver.domain.friends;

import com.ys.chatserver.domain.BaseTimeEntity;
import com.ys.chatserver.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Friends extends BaseTimeEntity {

    @Id
    private Long id;

    @Setter
    @OneToMany
    @JoinColumn(name="friends_name")
    private Set<User> friendSet;

    @Builder
    public Friends(Long id) {
        this.id = id;
        this.friendSet = new HashSet<>();
    }
}
