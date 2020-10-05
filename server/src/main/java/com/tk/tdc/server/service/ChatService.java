package com.tk.tdc.server.service;

import com.tk.tdc.server.domain.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface ChatService {

    Flux<Message> followChat(String room, Date initialTime);

    Mono<Message> insertMessage(Message message);

}
