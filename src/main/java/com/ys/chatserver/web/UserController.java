package com.ys.chatserver.web;

import com.ys.chatserver.common.ApiResponse;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.service.UserService;
import com.ys.chatserver.web.dto.UserInfoDto;
import com.ys.chatserver.web.dto.UserSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @RequestMapping("/api/v1/users")
    public ApiResponse getUser() {
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ApiResponse.success("user", user);
    }

    @GetMapping
    @RequestMapping("/api/v1/users/search")
    public ApiResponse searchUser(@RequestParam String email) {

        List<User> userList = userService.searchUser(email);

        List<UserInfoDto> userInfoDtoList = userList.stream()
                .map(user -> UserInfoDto.builder().user(user).build()).collect(Collectors.toList());

        UserSearchResponseDto userSearchResponseDto = UserSearchResponseDto.builder()
                .userInfoDtoList(userInfoDtoList)
                .build();

        return ApiResponse.success("userList", userSearchResponseDto);
    }
}
