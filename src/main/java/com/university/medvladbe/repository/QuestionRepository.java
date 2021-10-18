package com.university.medvladbe.repository;

import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.entity.question.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    @Query("SELECT qr.question FROM QuestionResult qr WHERE qr.verdict=true")
    List<Question> findActiveQuestions();

    @Query("SELECT qa from QuestionAnswer qa where qa.question= :question")
    List<QuestionAnswer> findAnswersForQuestion(@Param("question")Question question);

    @Query("Select q from Question q where q.checked=false")
    List<Question> findUncheckedQuestions();

}
