package com.ys.chatserver.web;

import com.ys.chatserver.domain.messages.Messages;
import com.ys.chatserver.domain.messages.MessagesRepository;
import com.ys.chatserver.web.dto.MessagesSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessagesApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MessagesRepository messagesRepository;

    @After
    public void tearDown() throws Exception {
        messagesRepository.deleteAll();
    }

    @Test
    public void Messages_등록된다() throws Exception {
        // given
        String content = "content";
        MessagesSaveRequestDto requestDto = MessagesSaveRequestDto.builder()
                .content(content)
                .author("author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/messages";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        List<Messages> all = messagesRepository.findAll();
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void Messages_삭제된다() throws Exception {
        // given
        String content = "content";
        MessagesSaveRequestDto requestDto = MessagesSaveRequestDto.builder()
                .content(content)
                .author("author")
                .build();

        String postUrl = "http://localhost:" + port + "/api/v1/messages";

        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(postUrl, requestDto, Long.class);
        List<Messages> all = messagesRepository.findAll();
        Long id = all.get(0).getId();

        String deleteUrl = postUrl + "/" + id;

        // when
        restTemplate.delete(deleteUrl);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        all = messagesRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }
}
