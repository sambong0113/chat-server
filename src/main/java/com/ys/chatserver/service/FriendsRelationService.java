package com.ys.chatserver.service;

import com.ys.chatserver.domain.friendsRelation.FriendsRelation;
import com.ys.chatserver.domain.friendsRelation.FriendsRelationRepository;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.web.dto.FriendsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class FriendsRelationService {

    private final FriendsRelationRepository friendsRelationRepository;

    private final UserService userService;

    public FriendsResponseDto getFriends() {

        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());
        Set<FriendsRelation> entity = friendsRelationRepository.findByFromUserSeq(user.getUserSeq());
        return new FriendsResponseDto(entity);
    }

    public Long save(Long friendId) {

        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

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
