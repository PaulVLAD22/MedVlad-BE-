package com.university.medvladbe.repository;

import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.entity.question.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer,Long> {
    List<QuestionAnswer> findByQuestion(Question question);
}