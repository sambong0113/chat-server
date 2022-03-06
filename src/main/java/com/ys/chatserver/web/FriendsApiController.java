package com.ys.chatserver.web;

import com.ys.chatserver.service.FriendsRelationService;
import com.ys.chatserver.web.dto.FriendsAddRequestDto;
import com.ys.chatserver.domain.user.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FriendsApiController {

    private final FriendsRelationService friendsRelationService;

    @GetMapping("/api/v1/friends")
    public ResponseEntity<List<UserInfoDto>> getFriends() {
        return ResponseEntity.ok()
                .body(friendsRelationService.getFriends());
    }

    @PutMapping("/api/v1/friends")
    public ResponseEntity<HashMap<String, Long>> addFriend(@RequestBody FriendsAddRequestDto requestDto) {
        Long friendId = requestDto.getFriendId();
        HashMap<String, Long> body = new HashMap<>();
        body.put("friendId", friendsRelationService.save(friendId));

        return ResponseEntity.ok()
                .body(body);
    }
}
