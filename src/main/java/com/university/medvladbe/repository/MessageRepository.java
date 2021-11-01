package com.university.medvladbe.repository;

import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    public List<Message> findAllBySender(User sender);
}
