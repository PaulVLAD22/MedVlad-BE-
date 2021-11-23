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

    private final List<String> doctors;
    private final List<String> users;

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
        doctors.add(doctor.getUsername());
        System.out.println(doctors);
        System.out.println("DA");
        while (users.size() == 0){
            Thread.sleep(1);
            System.out.println("User size 0");
        }
        topicNumber = 192002002;
        System.out.println("OUT");
        doctors.remove(username);
        return topicNumber;

    }


    public int userJoinsQueue(String username) throws InterruptedException {
        System.out.println(doctors);
        users.add(username);
        Thread.sleep(1000);
        users.remove(username);
        return topicNumber;


    }
}
