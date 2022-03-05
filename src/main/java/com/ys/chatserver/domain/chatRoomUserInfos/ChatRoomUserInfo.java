package com.ys.chatserver.domain.chatRoomUserInfos;

import com.ys.chatserver.domain.BaseTimeEntity;
import com.ys.chatserver.domain.chatRooms.ChatRoom;
import com.ys.chatserver.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ChatRoomUserInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
