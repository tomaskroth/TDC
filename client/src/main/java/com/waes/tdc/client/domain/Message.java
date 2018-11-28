package com.waes.tdc.client.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class Message {

    private String id;
    private String room;
    private String author;
    private String content;
    private Date time;

}
