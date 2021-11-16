package com.university.medvladbe.repository;

import com.university.medvladbe.model.entity.account.User;
import com.university.medvladbe.model.entity.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    public List<Message> findAllBySenderOrReceiver(User sender, User receiver);
    public List<Message> findAllBySenderAndReceiver(User sender, User receiver);
}
