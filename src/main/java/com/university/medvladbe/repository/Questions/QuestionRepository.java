package com.university.medvladbe.repository.Questions;

import com.university.medvladbe.model.entity.question.Question;
import com.university.medvladbe.model.entity.question.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> getQuestionByCheckedTrueAndAdmin_Username(String username);
    List<Question> getQuestionByAnswerNotNull();

    @Query("SELECT qr FROM Question qr WHERE qr.verdict=true")
    List<Question> findActiveQuestions();

    @Query("SELECT qa from QuestionAnswer qa where qa.question= :question")
    List<QuestionAnswer> findAnswersForQuestion(@Param("question")Question question);

    @Query("Select q from Question q where q.checked=false")
    List<Question> findUncheckedQuestions();


}
