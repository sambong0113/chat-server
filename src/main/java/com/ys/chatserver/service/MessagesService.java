package com.ys.chatserver.service;

import com.ys.chatserver.domain.messages.Messages;
import com.ys.chatserver.domain.messages.MessagesRepository;
import com.ys.chatserver.web.dto.MessagesResponseDto;
import com.ys.chatserver.web.dto.MessagesSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MessagesService {
    private final MessagesRepository messagesRepository;

    @Transactional
    public Long save(MessagesSaveRequestDto requestsDto) {
        return messagesRepository.save(requestsDto.toEntity()).getId();
    }

    public MessagesResponseDto findById(Long id) {
        Messages entity = messagesRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 메시지가 없습니다. id=" + id));
        return new MessagesResponseDto(entity);
    }

    public void deleteById(Long id) {
        messagesRepository.deleteById(id);
    }
}
