package com.ys.chatserver.domain.chatRoomUserInfos;

import com.ys.chatserver.domain.chatRooms.ChatRoom;
import com.ys.chatserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomUserInfoRepository extends JpaRepository<ChatRoomUserInfo, Long> {
    List<ChatRoomUserInfo> findByUser(User user);

    List<ChatRoomUserInfo> findByChatRoom(ChatRoom chatRoom);

    @Query("SELECT c.chatRoom FROM ChatRoomUserInfo c WHERE c.user IN (:userList) GROUP BY c.chatRoom HAVING count(c) = :userCount")
    List<ChatRoom> findByChatRoomWithUsersOnlyContained(@Param("userList") List<User> userList, @Param("userCount") Long userCount);
}
