package com.ys.chatserver.domain.messages;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<Messages, Long> {
}
