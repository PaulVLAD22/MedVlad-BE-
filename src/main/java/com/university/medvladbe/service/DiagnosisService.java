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

    public DiagnosisResultDto calculateCondition(List<Integer> selectedSymptoms) {
        List<Symptom> symptomList = symptomRepository.findAll();
        List<Question> questionLists = questionRepository.getQuestionByAnswerNotNull();

        Question similarQuestion = null;
        double maxSimilarity = -1;
        System.out.println("AICI");
        System.out.println(selectedSymptoms);
        for (Question question : questionLists) {
            List<Symptom> symptoms = question.getSymptoms();
            List<Integer> binaryArray = new ArrayList<>();
            for (int i = 0; i < symptomList.size(); i++) {
                binaryArray.add(0);
            }
            symptoms.forEach(symptom -> binaryArray.set(symptomList.indexOf(symptom), 1));
            if (similarity(binaryArray, selectedSymptoms) > maxSimilarity) {
                System.out.println(binaryArray);
                System.out.println(similarity(binaryArray, selectedSymptoms));
                maxSimilarity = similarity(binaryArray, selectedSymptoms);
                similarQuestion = question;
            }
        }


        return DiagnosisResultDto
                .builder()
                .condition(similarQuestion.getAnswer().getPacientCondition())
                .similarityScore(maxSimilarity)
                .similarQuestionDoctor(similarQuestion.getAnswer().getDoctor().userDtoFromUser())
                .build();
    }

    private double similarity(List<Integer> vectorA, List<Integer> vectorB) {
        int equalScore = 0;
        for (int i = 0; i < vectorA.size(); i++) {
            if (vectorA.get(i)==vectorB.get(i)) {
                if (vectorA.get(i) == 0) {
                    equalScore += 1;
                } else if (vectorA.get(i) == 1) {
                    equalScore += 5;
                }
            }
        }
        return equalScore;
    }
}
