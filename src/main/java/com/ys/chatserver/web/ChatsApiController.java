package com.ys.chatserver.web;

import com.ys.chatserver.service.ChatRoomService;
import com.ys.chatserver.web.dto.ChatCreateRequestDto;
import com.ys.chatserver.web.dto.ChatRoomsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatsApiController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/api/v1/chats")
    public ResponseEntity<List<ChatRoomsResponseDto>> listChatRooms() {
        return ResponseEntity.ok()
                .body(chatRoomService.listChatRooms());
    }

    @PostMapping("/api/v1/chats")
    public ResponseEntity<Long> createChatRoom(@RequestBody ChatCreateRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(chatRoomService.createChatRoom(requestDto));
    }
}
