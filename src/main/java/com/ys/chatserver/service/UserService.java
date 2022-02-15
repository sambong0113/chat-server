package com.ys.chatserver.service;

import com.ys.chatserver.domain.friends.Friends;
import com.ys.chatserver.domain.friends.FriendsRepository;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final FriendsRepository friendsRepository;

    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Transactional
    public User createUser(User user) {

        userRepository.save(user);

        Friends friends = Friends.builder()
                .id(user.getUserSeq())
                .build();

        friendsRepository.save(friends);

        return user;
    }
}
