package com.ys.chatserver.domain.friendsRelation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FriendsRelationRepository extends JpaRepository<FriendsRelation, Long> {
    Set<FriendsRelation> findByFromUserSeq(Long userSeq);

    FriendsRelation findByFromUserSeqAndToUserSeq(Long fromUserSeq, Long toUserSeq);
}
