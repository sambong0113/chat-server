package com.ys.chatserver.domain.friendsRelation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ys.chatserver.domain.BaseTimeEntity;
import com.ys.chatserver.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class FriendsRelation extends BaseTimeEntity {

    @JsonIgnore
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="from_user_seq")
    private User from;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="to_user_seq")
    private User to;

    @Builder
    public FriendsRelation(User from, User to) {
        this.from = from;
        this.to = to;
    }
}
