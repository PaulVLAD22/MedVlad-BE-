package com.university.medvladbe.entity.chat;

import lombok.Data;

import javax.persistence.Id;
import java.sql.Date;

@Data
public class ChatMessage {
    @Id
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
    private String senderName;
    private String recipientName;
    private String content;
    private Date timestamp;
    private MessageStatus status;
}

