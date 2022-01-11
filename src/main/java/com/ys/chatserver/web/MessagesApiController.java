package com.ys.chatserver.web;

import com.ys.chatserver.service.MessagesService;
import com.ys.chatserver.web.dto.MessagesResponseDto;
import com.ys.chatserver.web.dto.MessagesSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MessagesApiController {

    private final MessagesService messagesService;

    @PostMapping("/api/v1/messages")
    public Long save(@RequestBody MessagesSaveRequestDto requestDto) {
        return messagesService.save(requestDto);
    }

    @GetMapping("/api/v1/messages/{id}")
    public MessagesResponseDto findById(@PathVariable Long id) {
        return messagesService.findById(id);
    }

    @DeleteMapping("/api/v1/messages/{id}")
    public void delete(@PathVariable Long id) {
        messagesService.deleteById(id);
    }
}
