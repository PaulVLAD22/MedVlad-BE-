package com.university.medvladbe.service;

import com.university.medvladbe.model.entity.question.*;
import com.university.medvladbe.repository.Questions.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class DiagnosisService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SymptomRepository symptomRepository;

    public String calculateCondition(List<Integer> selectedSymptoms){
        List<Symptom> symptomList = symptomRepository.findAll();
        List<Question> questionLists = questionRepository.getQuestionByAnswerNotNull();
        questionLists.forEach(System.out::println);
        return "";
    }
}
