package com.ys.chatserver.service;

import com.ys.chatserver.domain.friends.Friends;
import com.ys.chatserver.domain.friends.FriendsRepository;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.domain.user.UserRepository;
import com.ys.chatserver.web.dto.FriendsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FriendsService {

    private final FriendsRepository friendsRepository;

    private final UserRepository userRepository;

    public FriendsResponseDto findById(Long id) {
        Friends entity = friendsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 친구 정보가 없습니다. id=" + id));
        return new FriendsResponseDto(entity);
    }

    public Long addFriend(Long id, Long friendId) {
        Friends entity = friendsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 친구 정보가 없습니다. id=" + id));

        User friendUser = userRepository.findByUserSeq(friendId);

        Set<User> newFriendsSet = entity.getFriendSet().stream().collect(Collectors.toSet());

        if (!newFriendsSet.add(friendUser)) {
            throw new IllegalArgumentException("이미 존재하는 친구입니다. id=" + id);
        }

        entity.setFriendSet(newFriendsSet);

        return friendsRepository.save(entity).getId();
    }
}
