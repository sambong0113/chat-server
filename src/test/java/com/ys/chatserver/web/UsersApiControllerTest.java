package com.ys.chatserver.web;

import com.ys.chatserver.auth.dto.ProviderType;
import com.ys.chatserver.config.properties.AppProperties;
import com.ys.chatserver.config.token.AuthToken;
import com.ys.chatserver.config.token.AuthTokenProvider;
import com.ys.chatserver.domain.friendsRelation.FriendsRelationRepository;
import com.ys.chatserver.domain.user.Role;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.domain.user.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendsRelationRepository friendsRelationRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AuthTokenProvider tokenProvider;

    @Autowired
    private AppProperties appProperties;

    private MockMvc mvc;

    private User user;

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

        userRepository.saveAndFlush(user);
    }

    @After
    public void tearDown() {
        friendsRelationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void User_가져온다() throws Exception {

        // given
        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
                user.getUserId(),
                user.getRoleKey(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String url = "http://localhost:" + port + "/api/v1/users";

        // when
        mvc.perform(get(url)
                .header("Authentication", "Bearer " + accessToken.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.user.name").value(user.getName()))
                .andExpect(jsonPath("$.body.user.email").value(user.getEmail()));
    }

    @Test
    @WithMockUser(roles="USER")
    public void User_검색한다() throws Exception {

        String url = "http://localhost:" + port + "/api/v1/users/search?email=test";

        // when
        MvcResult result = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.userList.userInfoDtoList[0].name").value(user.getName()))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
