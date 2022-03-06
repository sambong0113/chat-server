package com.ys.chatserver.service;

import com.ys.chatserver.domain.chatRoomUserInfos.ChatRoomUserInfo;
import com.ys.chatserver.domain.chatRoomUserInfos.ChatRoomUserInfoRepository;
import com.ys.chatserver.domain.chatRooms.ChatRoom;
import com.ys.chatserver.domain.chatRooms.ChatRoomRepository;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.web.dto.ChatCreateRequestDto;
import com.ys.chatserver.web.dto.ChatRoomsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomsRepository;
    private final ChatRoomUserInfoRepository chatRoomUserInfosRepository;
    private final UserService userService;

    public List<ChatRoomsResponseDto> listChatRooms() {

        User user = userService.getUser();

        return chatRoomUserInfosRepository.findByUser(user).stream()
                .map(chatRoomUserInfo -> {
                    ChatRoom chatRoom = chatRoomUserInfo.getChatRoom();
                    List<User> userList = chatRoomUserInfosRepository.findByChatRoom(chatRoom).stream()
                            .map(ChatRoomUserInfo::getUser)
                            .collect(Collectors.toList());

                    return new ChatRoomsResponseDto(chatRoom, userList);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createChatRoom(ChatCreateRequestDto chatCreateRequestDto) {

        User user = userService.getUser();
        User other = userService.getUser(chatCreateRequestDto.getUserId());

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(other);


        ChatRoom chatRoom = ChatRoom.builder().title(chatCreateRequestDto.getTitle()).build();
        chatRoomsRepository.save(chatRoom);

        List<ChatRoomUserInfo> userInfoList = userList.stream()
                .map(u -> ChatRoomUserInfo.builder().chatRoom(chatRoom).user(u).build())
                .collect(Collectors.toList());

        chatRoomUserInfosRepository.saveAll(userInfoList);

        return chatRoom.getId();
    }
}
