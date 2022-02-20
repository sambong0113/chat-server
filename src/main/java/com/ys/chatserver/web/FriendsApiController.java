package com.ys.chatserver.web;

import com.ys.chatserver.common.ApiResponse;
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
    public ApiResponse getFriends() {
        return ApiResponse.success("friends", friendsRelationService.getFriends());
    }

    @PutMapping("/api/v1/friends")
    public ApiResponse addFriend(@RequestBody FriendsAddRequestDto requestDto) {
        Long friendId = requestDto.getFriendId();
        return ApiResponse.success("friendId", friendsRelationService.save(friendId));
    }
}
