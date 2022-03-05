package com.ys.chatserver.service;

import com.ys.chatserver.domain.chatRoomUserInfos.ChatRoomUserInfoRepository;
import com.ys.chatserver.domain.chatRooms.ChatRoomRepository;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.web.dto.ChatRoomsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomsRepository;
    private final ChatRoomUserInfoRepository chatRoomUserInfosRepository;
    private final UserService userService;

    public List<ChatRoomsResponseDto> listChatRooms() {

        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return chatRoomUserInfosRepository.findByUser(user).stream()
                .map(chatRoomUserInfo -> chatRoomsRepository.findById(chatRoomUserInfo.getId()).orElse(null))
                .filter(Objects::nonNull)
                .map(ChatRoomsResponseDto::new)
                .collect(Collectors.toList());
    }



}
