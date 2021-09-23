package com.university.medvladbe.repository;

import com.university.medvladbe.entity.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email or u.username = :username")
    Optional<User> findUserByEmailOrUsername(@Param("email") String email, @Param("username")String username);

}
