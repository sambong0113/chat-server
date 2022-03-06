package com.ys.chatserver.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.chatserver.auth.dto.ProviderType;
import com.ys.chatserver.config.properties.AppProperties;
import com.ys.chatserver.config.token.AuthToken;
import com.ys.chatserver.config.token.AuthTokenProvider;
import com.ys.chatserver.domain.chatRoomUserInfos.ChatRoomUserInfo;
import com.ys.chatserver.domain.chatRoomUserInfos.ChatRoomUserInfoRepository;
import com.ys.chatserver.domain.chatRooms.ChatRoom;
import com.ys.chatserver.domain.chatRooms.ChatRoomRepository;
import com.ys.chatserver.domain.friendsRelation.FriendsRelationRepository;
import com.ys.chatserver.domain.user.Role;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.domain.user.UserRepository;
import com.ys.chatserver.web.dto.ChatCreateRequestDto;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatRoomUserInfoRepository chatRoomUserInfoRepository;

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
        chatRoomUserInfoRepository.deleteAll();
        chatRoomRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void chatsRoomWillBeCreatedSuccessfully() throws Exception {

        // given
        ChatCreateRequestDto chatCreateRequestDto = new ChatCreateRequestDto("", friendUser.getUserSeq());

        String url = "http://localhost:" + port + "/api/v1/chats";

        // when
        mvc.perform(post(url)
                .header("Authentication", "Bearer " + accessToken.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(chatCreateRequestDto)))
                .andExpect(status().isOk());

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(friendUser);

        // then
        ChatRoom chatRoom = chatRoomUserInfoRepository.findByChatRoomWithUsersOnlyContained(userList, (long) userList.size()).stream().findFirst().orElse(null);
        assertThat(chatRoom).isNotNull();
    }

    @Test
    @Transactional
    public void listChatRoomWillGetSuccessfully() throws Exception {

        // given
        String url = "http://localhost:" + port + "/api/v1/chats";

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(friendUser);

        ChatRoom chatRoom = ChatRoom.builder().title("test").build();
        chatRoomRepository.save(chatRoom);

        List<ChatRoomUserInfo> chatRoomUserInfoList = userList.stream()
                .map(u -> ChatRoomUserInfo.builder().user(u).chatRoom(chatRoom).build())
                .collect(Collectors.toList());
        chatRoomUserInfoRepository.saveAll(chatRoomUserInfoList);

        // when
        mvc.perform(get(url)
                .header("Authentication", "Bearer " + accessToken.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value("test"));
    }
}
