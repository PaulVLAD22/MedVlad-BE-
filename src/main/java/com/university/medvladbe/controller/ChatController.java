package com.university.medvladbe.controller;

import com.university.medvladbe.model.chat.ChatMessage;
import com.university.medvladbe.service.*;
import com.university.medvladbe.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class ChatController {
    @Autowired
    private ChatMatchingService chatMatchingService;

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload final ChatMessage chatMessage){
        log.info("COX");
        return chatMessage;
    }
    @MessageMapping("/chat.newUser")
    @SendTo("/topic/public")
    public ChatMessage newUser(@Payload final ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor){
        log.info("NEW USER");
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }
    @GetMapping("/doctor/joinQueue")
    @ResponseBody
    public int getDoctorTopic() throws InterruptedException {
        System.out.println("KLKL");
        String username = UserMethods.getCurrentUsername();
        System.out.println("JKJJ");
        return chatMatchingService.doctorJoinsQueue(username);
    }
    @GetMapping("/user/joinQueue")
    @ResponseBody
    public int getUserTopic() throws InterruptedException {
        String username = UserMethods.getCurrentUsername();
        return chatMatchingService.userJoinsQueue();
    }
}
