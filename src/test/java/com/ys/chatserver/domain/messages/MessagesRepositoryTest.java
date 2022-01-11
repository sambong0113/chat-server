package com.ys.chatserver.domain.messages;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessagesRepositoryTest {

    @Autowired
    MessagesRepository messagesRepository;

    @After
    public void cleanup() {
        messagesRepository.deleteAll();
    }

    @Test
    public void 메시지저장_불러오기() {
        // given
        String content = "테스트";

        messagesRepository.save(Messages.builder()
                .content(content)
                .author("test@test.com")
                .build());

        // when
        List<Messages> messagesList = messagesRepository.findAll();

        // then
        Messages messages = messagesList.get(0);
        assertThat(messages.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity_등록() {
        // given
        LocalDateTime now = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        messagesRepository.save(Messages.builder()
                .content("content")
                .author("author")
                .build());

        // when
        List<Messages> messagesList = messagesRepository.findAll();

        // then
        Messages messages = messagesList.get(0);

        System.out.println(">>>>>>>> createdDate=" + messages.getCreatedDate() + ", modifiedDate=" + messages.getModifiedDate());

        assertThat(messages.getCreatedDate()).isAfter(now);
        assertThat(messages.getModifiedDate()).isAfter(now);

    }
}