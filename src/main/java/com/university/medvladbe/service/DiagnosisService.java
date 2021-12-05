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

        Question similarQuestion = null;
        double maxSimilarity = 0 ;
        for (Question question : questionLists) {
            List<Symptom> symptoms = question.getSymptoms();
            List<Integer> binaryArray = new ArrayList<>();
            for (int i = 0; i < symptomList.size(); i++) {
                binaryArray.add(0);
            }
            symptoms.forEach(symptom -> binaryArray.set(symptomList.indexOf(symptom), 1));
            if (cosineSimilarity(binaryArray, selectedSymptoms) > maxSimilarity) {
                maxSimilarity = cosineSimilarity(binaryArray, selectedSymptoms);
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
    private double cosineSimilarity(List<Integer> vectorA, List<Integer> vectorB) {
        int dotProduct = 0;
        int normA = 0, normB = 0;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
