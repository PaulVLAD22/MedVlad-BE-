package com.university.medvladbe.service;

import com.university.medvladbe.model.entity.account.*;
import com.university.medvladbe.repository.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@Slf4j
public class ChatMatchingService {
    @Autowired
    private UserRepository userRepository;

    private final List<User> doctors;
    private final List<User> users;

    private int topicNumber;

    public ChatMatchingService(){
        doctors = new ArrayList<>();
        users = new ArrayList<>();
    }

    public int doctorJoinsQueue(String username) throws InterruptedException {
        System.out.println("KL");
        User doctor = userRepository.findByUsername(username);
        System.out.println("NOPE");
        // stack overflow
        doctors.add(doctor);
        System.out.println(doctors);
        System.out.println("DA");
        while (users.size() == 0)
            Thread.sleep(5000);

        return topicNumber;

    }


    public int userJoinsQueue() throws InterruptedException {
        System.out.println(doctors);
        while (doctors.size() == 0) {
            Thread.sleep(5000);
        }
        topicNumber = 1920002;
        return topicNumber;


    }
}
