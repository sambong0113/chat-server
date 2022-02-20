package com.ys.chatserver.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.chatserver.auth.dto.ProviderType;
import com.ys.chatserver.config.properties.AppProperties;
import com.ys.chatserver.config.token.AuthToken;
import com.ys.chatserver.config.token.AuthTokenProvider;
import com.ys.chatserver.domain.friendsRelation.FriendsRelation;
import com.ys.chatserver.domain.friendsRelation.FriendsRelationRepository;
import com.ys.chatserver.domain.user.Role;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.domain.user.UserRepository;
import com.ys.chatserver.web.dto.FriendsAddRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendsRelationApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FriendsRelationRepository friendsRelationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AuthTokenProvider tokenProvider;

    @Autowired
    private AppProperties appProperties;

    private MockMvc mvc;

    private User user;
    private User friendUser;
    private AuthToken accessToken;

    @Before
    public void setup() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // given
        user = new User(
                "test",
                "test",
                "",
                "test@test.com",
                "Y",
                "",
                ProviderType.GOOGLE,
                Role.USER
        );
        userRepository.save(user);

        friendUser = new User(
                "testFriend",
                "testFriend",
                "",
                "test@test.com",
                "Y",
                "",
                ProviderType.GOOGLE,
                Role.USER
        );
        userRepository.save(friendUser);

        Date now = new Date();
        accessToken = tokenProvider.createAuthToken(
                user.getUserId(),
                user.getRoleKey(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
        friendsRelationRepository.deleteAll();
    }

    @Test
    @Transactional
    public void Friends_가져온다() throws Exception {

        // given
        FriendsRelation friendsRelation = FriendsRelation.builder()
                .from(user)
                .to(friendUser)
                .build();

        friendsRelationRepository.save(friendsRelation);

        String url = "http://localhost:" + port + "/api/v1/friends";

        // when
        mvc.perform(get(url)
                .header("Authentication", "Bearer " + accessToken.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.friends.friendSet[0].name").value(friendUser.getName()));
    }

    @Test
    public void Friends_추가한다() throws Exception {

        String url = "http://localhost:" + port + "/api/v1/friends";

        FriendsAddRequestDto requestDto = FriendsAddRequestDto.builder()
                .friendId(friendUser.getUserSeq())
                .build();

        // when
        mvc.perform(put(url)
                .header("Authentication", "Bearer " + accessToken.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // then
        FriendsRelation friendsRelation = friendsRelationRepository.findByFromUserSeqAndToUserSeq(user.getUserSeq(), friendUser.getUserSeq());
        assertThat(friendsRelation).isNotNull();
    }
}
