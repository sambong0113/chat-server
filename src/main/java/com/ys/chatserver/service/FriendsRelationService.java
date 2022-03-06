package com.ys.chatserver.service;

import com.ys.chatserver.domain.friendsRelation.FriendsRelation;
import com.ys.chatserver.domain.friendsRelation.FriendsRelationRepository;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.domain.user.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FriendsRelationService {

    private final FriendsRelationRepository friendsRelationRepository;

    private final UserService userService;

    public List<UserInfoDto> getFriends() {

        UserInfoDto user = userService.getUserDto();
        return friendsRelationRepository.findByFromUserSeq(user.getUserSeq()).stream()
                .map(FriendsRelation::getTo)
                .map(UserInfoDto::new)
                .collect(Collectors.toList());
    }

    public Long save(Long friendId) {

        UserInfoDto user = userService.getUserDto();

        FriendsRelation entity = friendsRelationRepository.findByFromUserSeqAndToUserSeq(user.getUserSeq(), friendId);
        if (entity != null) {
            throw new IllegalArgumentException("이미 존재하는 친구입니다. id=" + friendId);
        }

        User from = userService.getUser(user.getUserSeq());
        User to = userService.getUser(friendId);

        FriendsRelation friendsRelation = FriendsRelation.builder()
                .from(from)
                .to(to)
                .build();

        return friendsRelationRepository.save(friendsRelation).getId();
    }
}
