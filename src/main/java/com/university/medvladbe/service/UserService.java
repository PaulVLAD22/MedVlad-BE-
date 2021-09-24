package com.university.medvladbe.service;

import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.exception.BadLogin;
import com.university.medvladbe.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @SneakyThrows
    public User login(String usernameOrEmail, String password){
        Optional<User> user = userRepository.findUserByEmailOrUsername(usernameOrEmail, password);
        return user.orElse(user.orElseThrow(BadLogin::new));
    }
}
