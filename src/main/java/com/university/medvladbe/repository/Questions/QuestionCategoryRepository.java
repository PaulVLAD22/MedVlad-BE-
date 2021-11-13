package com.university.medvladbe.repository.Questions;

import com.university.medvladbe.entity.question.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory,Long> {
    QuestionCategory findByName(String name);
}
