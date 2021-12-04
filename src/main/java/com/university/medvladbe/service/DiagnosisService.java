package com.university.medvladbe.service;

import com.university.medvladbe.dto.*;
import com.university.medvladbe.model.entity.account.*;
import com.university.medvladbe.model.entity.question.*;
import com.university.medvladbe.repository.*;
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
    @Autowired
    private UserRepository userRepository;

    public DiagnosisResultDto calculateCondition(List<Integer> selectedSymptoms){
        List<Symptom> symptomList = symptomRepository.findAll();
        List<Question> questionLists = questionRepository.getQuestionByAnswerNotNull();
        questionLists.forEach(System.out::println);
        User doctor = userRepository.findByUsername("doctor");
        String condition = "aids";
        int similarityScore = 90;
        return DiagnosisResultDto
                .builder()
                .condition(condition)
                .similarityScore(similarityScore)
                .similarQuestionDoctor(doctor.userDtoFromUser())
                .build();
    }
}
