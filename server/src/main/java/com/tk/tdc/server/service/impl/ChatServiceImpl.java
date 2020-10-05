package com.tk.tdc.server.service.impl;

import com.tk.tdc.server.domain.Message;
import com.tk.tdc.server.repository.MessageRepository;
import com.tk.tdc.server.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final MessageRepository messageRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @PostConstruct
    public void init() {
//        reactiveMongoTemplate
//                .dropCollection("messages")
//                .then(
//                        reactiveMongoTemplate
//                                .createCollection(
//                                        "messages",
//                                        CollectionOptions
//                                                .empty()
//                                                .capped()
//                                                .size(2048)
//                                                .maxDocuments(10000)
//                                )
//                )
//                .subscribe();
    }

    @Override
    public Flux<Message> followChat(String room, Date initialTime) {
        return messageRepository.findByRoomAndTimeGreaterThan(room, initialTime);
    }

    @Override
    public Mono<Message> insertMessage(Message message) {
        Date date = new Date();
        message.setTime(date);
        return messageRepository.insert(message);
    }
}
