package com.ys.chatserver.service;

import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.domain.user.UserRepository;
import com.ys.chatserver.web.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserInfoDto getUser() {
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new UserInfoDto(userRepository.findByUserId(principal.getUsername()));
    }

    public User getUser(Long userSeq) {
        return userRepository.findByUserSeq(userSeq);
    }

    public List<UserInfoDto> searchUser(String emailPrefix) {
        return userRepository.findByEmailStartsWith(emailPrefix).stream().map(UserInfoDto::new).collect(Collectors.toList());
    }
}
