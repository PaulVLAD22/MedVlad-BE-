package com.university.medvladbe.controller;

import com.university.medvladbe.entity.chat.ChatMessage;
import com.university.medvladbe.entity.chat.ChatNotification;
import com.university.medvladbe.entity.chat.MessageModel;
import com.university.medvladbe.service.Chat.ChatMessageService;
import com.university.medvladbe.service.Chat.ChatRoomService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
//
//    @MessageMapping("/chat/{to}")
//    public void sendMessage(@DestinationVariable String to, MessageModel message) {
//        System.out.println("handling send message: " + message + " to: " + to);
//        boolean isExists = UserStorage.getInstance().getUsers().contains(to);
//        if (isExists) {
//            simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
//        }
//    }
}