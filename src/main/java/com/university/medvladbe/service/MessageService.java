package com.university.medvladbe.service;

import com.university.medvladbe.dto.MessageDto;
import com.university.medvladbe.model.entity.account.User;
import com.university.medvladbe.model.entity.message.Message;
import com.university.medvladbe.repository.MessageRepository;
import com.university.medvladbe.repository.UserRepository;
import com.university.medvladbe.util.UserMethods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<MessageDto> getMessagesWithUser(String loggedUsername, String username2) {
        User loggedUser = userRepository.findByUsername(loggedUsername);
        User otherUser = userRepository.findByUsername(username2);
        List<Message> receivedMessages = messageRepository.findAllBySenderAndReceiver(otherUser, loggedUser);
        List<Message> sentMessages = messageRepository.findAllBySenderAndReceiver(loggedUser, otherUser);
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(receivedMessages);
        allMessages.addAll(sentMessages);
        List<MessageDto> messageDtos = new ArrayList<>();

        allMessages.forEach(message -> {
            messageDtos.add(MessageDto.builder()
                    .receiverUsername(message.getReceiver().getUsername())
                    .senderUsername(message.getSender().getUsername())
                    .content(message.getContent())
                    .timeOfSending(message.getTimeOfSending())
                    .build());
        });

        return messageDtos;


    }

    public List<MessageDto> getLastMessagesForUser(String username) {
        log.info("ADMIPP");
        List<Message> messages = messageRepository.findAllBySenderOrReceiver
                (userRepository.findByUsername(username), userRepository.findByUsername(username));
        List<MessageDto> messageDtos = new ArrayList<>();
        Set<String> interactedWithUsers = new HashSet<>();
        log.info("PAPA");
        messages.forEach(message ->
        {
            interactedWithUsers.add(message.getReceiver().getUsername());
            interactedWithUsers.add(message.getSender().getUsername());
        });
        log.info("ADA");

        interactedWithUsers.remove(UserMethods.getCurrentUsername());

        for (String interactedWithUser : interactedWithUsers) {

            Message latestMessage = messages.stream()
                    .filter(message -> message.getReceiver().getUsername().equals(interactedWithUser)
                            || message.getSender().getUsername().equals(interactedWithUser))
                    .max(Comparator.comparing(Message::getTimeOfSending)).get();

            messageDtos.add(MessageDto.builder()
                    .receiverUsername(latestMessage.getReceiver().getUsername())
                    .senderUsername(latestMessage.getSender().getUsername())
                    .content(latestMessage.getContent())
                    .timeOfSending(latestMessage.getTimeOfSending())
                    .build());
        }
        messageDtos.forEach(System.out::println);
        return messageDtos;
    }

    public void postMessage(String senderUsername, String content, String receiverUsername) {
        log.info("COX");
        Message message = Message.builder()
                .content(content)
                .sender(userRepository.findByUsername(senderUsername))
                .receiver(userRepository.findByUsername(receiverUsername))
                .timeOfSending(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        messageRepository.save(message);
    }
}
