package com.ys.chatserver.domain.messages;

import com.ys.chatserver.domain.BaseTimeEntity;
import com.ys.chatserver.domain.chatRooms.ChatRoom;
import com.ys.chatserver.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private ChatRoom chatRoom;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Builder
    public Message(String content, User user, ChatRoom chatRoom) {
        this.content = content;
        this.user = user;
        this.chatRoom = chatRoom;
    }
}
