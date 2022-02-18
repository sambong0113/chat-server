package com.ys.chatserver.web;

import com.ys.chatserver.service.FriendsRelationService;
import com.ys.chatserver.web.dto.FriendsAddRequestDto;
import com.ys.chatserver.web.dto.FriendsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class FriendsApiController {

    private final FriendsRelationService friendsRelationService;

    @GetMapping("/api/v1/friends")
    public FriendsResponseDto getFriends() {
        return friendsRelationService.getFriends();
    }

    @PutMapping("/api/v1/friends")
    public Long addFriend(@RequestBody FriendsAddRequestDto requestDto) {
        Long friendId = requestDto.getFriendId();
        return friendsRelationService.save(friendId);
    }
}
