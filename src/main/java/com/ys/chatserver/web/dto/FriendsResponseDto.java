package com.ys.chatserver.web.dto;

import com.ys.chatserver.domain.friendsRelation.FriendsRelation;
import com.ys.chatserver.domain.user.User;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class FriendsResponseDto {
    private Set<User> friendSet;

    public FriendsResponseDto(Set<FriendsRelation> relationSet) {
        this.friendSet = relationSet.stream().map(FriendsRelation::getTo).collect(Collectors.toSet());
    }
}
