package com.university.medvladbe.repository;

import com.university.medvladbe.entity.question.QuestionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionResultRepository extends JpaRepository<QuestionResult,Long> {
}
