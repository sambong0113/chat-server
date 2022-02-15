package com.ys.chatserver.web;

import com.ys.chatserver.service.FriendsService;
import com.ys.chatserver.web.dto.FriendsAddRequestDto;
import com.ys.chatserver.web.dto.FriendsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class FriendsApiController {

    private final FriendsService friendsService;

    @GetMapping("/api/v1/friends/{id}")
    public FriendsResponseDto findById(@PathVariable Long id) {
        return friendsService.findById(id);
    }

    @PutMapping("/api/v1/friends")
    public Long addFriend(@RequestBody FriendsAddRequestDto requestDto) {
        Long userId = requestDto.getUserId();
        Long friendId = requestDto.getFriendId();
        return friendsService.addFriend(userId, friendId);
    }
}
