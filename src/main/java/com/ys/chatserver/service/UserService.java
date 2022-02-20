package com.ys.chatserver.service;

import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }
    public User getUser(Long userSeq) {
        return userRepository.findByUserSeq(userSeq);
    }
    public List<User> searchUser(String emailPrefix) {
        return userRepository.findByEmailStartsWith(emailPrefix);
    }
}
