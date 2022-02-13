package com.ys.chatserver.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.chatserver.auth.dto.OAuth2UserInfo;
import com.ys.chatserver.auth.dto.ProviderType;
import com.ys.chatserver.auth.service.CustomOAuth2UserService;
import com.ys.chatserver.config.properties.AppProperties;
import com.ys.chatserver.config.token.AuthToken;
import com.ys.chatserver.config.token.AuthTokenProvider;
import com.ys.chatserver.domain.messages.Messages;
import com.ys.chatserver.domain.messages.MessagesRepository;
import com.ys.chatserver.domain.user.Role;
import com.ys.chatserver.domain.user.User;
import com.ys.chatserver.domain.user.UserPrincipal;
import com.ys.chatserver.domain.user.UserRepository;
import com.ys.chatserver.web.dto.MessagesSaveRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AuthTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppProperties appProperties;

    private MockMvc mvc;
    @Before
    public void setup() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }


    @Test
    public void User_가져온다() throws Exception {

        // given
        User user = new User(
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
}