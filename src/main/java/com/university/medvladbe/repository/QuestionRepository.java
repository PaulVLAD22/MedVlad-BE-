package com.university.medvladbe.repository;

import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {


}
