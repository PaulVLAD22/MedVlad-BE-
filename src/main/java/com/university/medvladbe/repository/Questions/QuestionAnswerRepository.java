package com.university.medvladbe.repository.Questions;

import com.university.medvladbe.model.entity.question.Question;
import com.university.medvladbe.model.entity.question.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer,Long> {
    List<QuestionAnswer> findByQuestion(Question question);
}