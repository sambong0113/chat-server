package com.ys.chatserver.domain.chatRooms;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomsRepository  extends JpaRepository<ChatRooms, Long> {
}
