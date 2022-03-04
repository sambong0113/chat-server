package com.ys.chatserver.domain.chatRoomUserInfos;

import com.ys.chatserver.domain.BaseTimeEntity;
import com.ys.chatserver.domain.chatRooms.ChatRooms;
import com.ys.chatserver.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ChatRoomUserInfos extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRooms chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
