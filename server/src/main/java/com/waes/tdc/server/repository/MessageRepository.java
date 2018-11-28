package com.waes.tdc.server.repository;

import com.waes.tdc.server.domain.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

import java.util.Date;

public interface MessageRepository extends ReactiveMongoRepository<Message, String> {

    @Tailable
    Flux<Message> findByRoomAndTimeGreaterThan(String room, Date time);

}
