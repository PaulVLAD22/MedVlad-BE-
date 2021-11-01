package com.university.medvladbe.service;

import com.university.medvladbe.dto.MessageDto;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.message.Message;
import com.university.medvladbe.repository.MessageRepository;
import com.university.medvladbe.repository.UserRepository;
import com.university.medvladbe.util.UserMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;


    public List<MessageDto> getMessagesForUser(String username) {
        List<Message> messages = messageRepository.findAllBySender(userRepository.findByUsername(username));
        List<MessageDto> messageDtos = new ArrayList<>();
        Set<String> interactedWithUsers = new HashSet<>();
        messages.forEach(message ->
        {
            interactedWithUsers.add(message.getReceiver().getUsername());
            interactedWithUsers.add(message.getSender().getUsername());
        });

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
        Message message = Message.builder()
                .content(content)
                .sender(userRepository.findByUsername(senderUsername))
                .receiver(userRepository.findByUsername(receiverUsername))
                .timeOfSending(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        messageRepository.save(message);
    }
}
