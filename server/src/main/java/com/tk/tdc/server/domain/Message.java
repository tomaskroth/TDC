package com.tk.tdc.server.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "messages")
public class Message {

    @Id
    private String id;
    private String room;
    private String author;
    private String content;
    private Date time;

}
