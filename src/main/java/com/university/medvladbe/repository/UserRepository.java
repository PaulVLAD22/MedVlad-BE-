package com.university.medvladbe.repository;

import com.university.medvladbe.model.entity.account.Role;
import com.university.medvladbe.model.entity.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findUserByActiveFalse();
    User findFirstByActiveFalseAndRole(Role role);
}
