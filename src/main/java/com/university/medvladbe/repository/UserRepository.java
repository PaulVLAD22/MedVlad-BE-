package com.university.medvladbe.repository;

import com.university.medvladbe.entity.account.Role;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.Question;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    List<User> findUserByActiveFalse();
    User findFirstByActiveFalseAndRole(Role role);
}
