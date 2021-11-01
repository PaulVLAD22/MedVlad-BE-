package com.university.medvladbe.controller;

import com.university.medvladbe.dto.MessageDto;
import com.university.medvladbe.entity.message.Message;
import com.university.medvladbe.service.MessageService;
import com.university.medvladbe.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.university.medvladbe.util.UserMethods.getCurrentUsername;

@RestController
@Slf4j
public class MessageController {
    private UserDetailsServiceImpl userService;
    private MessageService messageService;

    @Autowired
    public MessageController(UserDetailsServiceImpl userService,
                             MessageService messageService
    ) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/getMessages")
    public List<MessageDto> getMessages(){
        return messageService.getMessagesForUser(getCurrentUsername());
    }
    @PostMapping("/postMessage")
    public void postMessage(@RequestParam String content, @RequestParam String receiverUsername){
        messageService.postMessage(getCurrentUsername(),content,receiverUsername);
    }
}
