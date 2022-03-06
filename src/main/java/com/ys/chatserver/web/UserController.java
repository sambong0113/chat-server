package com.ys.chatserver.web;

import com.ys.chatserver.service.UserService;
import com.ys.chatserver.domain.user.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @RequestMapping("/api/v1/users")
    public ResponseEntity<UserInfoDto> getUser() {

        return ResponseEntity.ok()
                .body(userService.getUserDto());
    }

    @GetMapping
    @RequestMapping("/api/v1/users/search")
    public ResponseEntity<List<UserInfoDto>> searchUser(@RequestParam String email) {

        List<UserInfoDto> userList = userService.searchUser(email);

        return ResponseEntity.ok()
                .body(userList);
    }
}
