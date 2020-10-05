package com.tk.tdc.server.controller;

import com.tk.tdc.server.domain.Message;
import com.tk.tdc.server.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatRestService {

    private final ChatService chatService;

    @PostMapping
    public Mono<Message> addMessage(@RequestBody Message message) {
        return chatService.insertMessage(message);
    }

    @GetMapping(value = "/room/{room}/stream", produces = {MediaType.APPLICATION_STREAM_JSON_VALUE})
    public Flux<Message> stream(@PathVariable("room") String room){
        Date initialTime = new Date();
        return chatService.followChat(room, initialTime);
    }

}

