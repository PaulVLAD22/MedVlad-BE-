package com.university.medvladbe.repository;

import com.university.medvladbe.entity.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE u.email = :usernameOrEmail or u.username = :usernameOrEmail and u.password = :password")
    Optional<User> findUserByEmailOrUsername(@Param("usernameOrEmail") String usernameOrEmail,@Param("password") String password);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);
}
