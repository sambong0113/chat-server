package com.ys.chatserver.domain.chatRoomUserInfos;

import com.ys.chatserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomUserInfoRepository extends JpaRepository<ChatRoomUserInfo, Long> {
    List<ChatRoomUserInfo> findByUser(User user);
}
