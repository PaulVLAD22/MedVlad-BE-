package com.university.medvladbe.repository;

import com.university.medvladbe.model.entity.registration.RegistrationResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationResultRepository extends JpaRepository<RegistrationResult,Long> {
    List<RegistrationResult> findAllByAdmin_Username(String adminUsername);
}
