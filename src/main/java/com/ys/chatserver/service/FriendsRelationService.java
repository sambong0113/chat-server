package com.ys.chatserver.service;

import com.ys.chatserver.domain.friendsRelation.FriendsRelation;
import com.ys.chatserver.domain.friendsRelation.FriendsRelationRepository;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.domain.user.UserRepository;
import com.ys.chatserver.web.dto.FriendsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class FriendsRelationService {

    private final FriendsRelationRepository friendsRelationRepository;

    private final UserRepository userRepository;

    public FriendsResponseDto findById(Long id) {
        Set<FriendsRelation> entity = friendsRelationRepository.findByFromUserSeq(id);
        return new FriendsResponseDto(entity);
    }

    public Long save(Long id, Long friendId) {
        FriendsRelation entity = friendsRelationRepository.findByFromUserSeqAndToUserSeq(id, friendId);
        if (entity != null) {
            throw new IllegalArgumentException("이미 존재하는 친구입니다. id=" + id);
        }

        User from = userRepository.findByUserSeq(id);
        User to = userRepository.findByUserSeq(friendId);

        FriendsRelation friendsRelation = FriendsRelation.builder()
                .from(from)
                .to(to)
                .build();

        return friendsRelationRepository.save(friendsRelation).getId();
    }
}
