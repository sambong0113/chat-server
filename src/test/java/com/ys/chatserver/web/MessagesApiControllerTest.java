package com.ys.chatserver.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.chatserver.config.token.AuthToken;
import com.ys.chatserver.config.token.AuthTokenProvider;
import com.ys.chatserver.domain.messages.Messages;
import com.ys.chatserver.domain.messages.MessagesRepository;
import com.ys.chatserver.web.dto.MessagesSaveRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessagesApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AuthTokenProvider tokenProvider;

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
        messagesRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER")
    public void Messages_등록된다() throws Exception {

        Date now = new Date();

        AuthToken accessToken = tokenProvider.createAuthToken(
                "100710616337397520819",
                "USER",
                new Date(now.getTime() + 1800000)
        );

        // given
        String content = "content";
        MessagesSaveRequestDto requestDto = MessagesSaveRequestDto.builder()
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/messages";

        String s = new ObjectMapper().writeValueAsString(requestDto);
        // when
        mvc.perform(post(url)
                .header("Authorization", "Bearer " + accessToken.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // then
        List<Messages> all = messagesRepository.findAll();
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles="USER")
    public void Messages_삭제된다() throws Exception {
        // given
        String content = "content";
        MessagesSaveRequestDto requestDto = MessagesSaveRequestDto.builder()
                .content(content)
                .author("author")
                .build();

        String postUrl = "http://localhost:" + port + "/api/v1/messages";
        mvc.perform(post(postUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)));

        List<Messages> all = messagesRepository.findAll();
        Long id = all.get(0).getId();

        String deleteUrl = postUrl + "/" + id;

        // when
        mvc.perform(delete(deleteUrl)).andExpect(status().isOk());

        // then
        all = messagesRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }
}
