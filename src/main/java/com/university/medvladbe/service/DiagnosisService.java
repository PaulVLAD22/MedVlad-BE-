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

    public List<DiagnosisResultDto> calculateCondition(List<Integer> selectedSymptoms) {
        List<Symptom> symptomList = symptomRepository.findAll();
        List<Question> questionLists = questionRepository.getQuestionByAnswerNotNull();

        Question similarQuestion = null;
        double maxSimilarity = -1;
        for (Question question : questionLists) {
            List<Symptom> symptoms = question.getSymptoms();
            List<Integer> binaryArray = new ArrayList<>();
            for (int i = 0; i < symptomList.size(); i++) {
                binaryArray.add(0);
            }
            symptoms.forEach(symptom -> binaryArray.set(symptomList.indexOf(symptom), 1));
            // binary array is binary array of all symptoms
            if (similarity(binaryArray, selectedSymptoms) > maxSimilarity) {
                maxSimilarity = similarity(binaryArray, selectedSymptoms);
                similarQuestion = question;
            }
        }
//        return DiagnosisResultDto
//                .builder()
//                .condition(similarQuestion.getAnswer().getPacientCondition())
//                .similarityScore(maxSimilarity)
//                .similarQuestionDoctor(similarQuestion.getAnswer().getDoctor().userDtoFromUser())
//                .build();

        List<DiagnosisResultDto> diagnosisResultDtos = new ArrayList<>();


        // diagnosis Result Dtos holds every
        for (Question question : questionLists) {
            List<Symptom> symptoms = question.getSymptoms();
            List<Integer> binaryArray = new ArrayList<>();
            for (int i = 0; i < symptomList.size(); i++) {
                binaryArray.add(0);
            }
            symptoms.forEach(symptom -> binaryArray.set(symptomList.indexOf(symptom), 1));
            // binary array is binary array of all symptoms
            if (similarity(binaryArray, selectedSymptoms) == maxSimilarity) {
                diagnosisResultDtos.add(DiagnosisResultDto.builder()
                        .condition(question.getAnswer().getPacientCondition())
                        .similarityScore(maxSimilarity)
                        .similarQuestionDoctor(question.getAnswer().getDoctor().userDtoFromUser())
                        .build());
            }
        }
        return diagnosisResultDtos;



    }

    //https://en.wikipedia.org/wiki/Jaccard_index
    //
    //https://stats.stackexchange.com/questions/550857/which-method-is-best-to-find-the-correlation-between-2-datasets-in-my-case?noredirect=1&lq=1

    private double similarity(List<Integer> vectorA, List<Integer> vectorB) {
//        int equalScore = 0;
//        for (int i = 0; i < vectorA.size(); i++) {
//            if (vectorA.get(i)==vectorB.get(i)) {
//                if (vectorA.get(i) == 0) {
//                    equalScore += 1;
//                } else if (vectorA.get(i) == 1) {
//                    equalScore += 5;
//                }
//            }
//        }
//        return equalScore;
//        double similarity = 1 - jaccardDistance(vectorA,vectorB);
        double similarity = 1-scalarProduct(vectorA,vectorB)/vectorA.size();
        return similarity;

    }

    private double scalarProduct(List<Integer> vectorA, List<Integer> vectorB){
        int out=0;
        for (int i = 0; i < vectorA.size(); i++) {
            if (vectorA.get(i)*vectorB.get(i)==1){
                out+=1;
            }
        }
        return out;
    }

    // incerca hamming distance
    private double hammingDistance(List<Integer> vectorA, List<Integer> vectorB){
        int out=0;
        for (int i = 0; i < vectorA.size(); i++) {
            if (vectorA.get(i)!=vectorB.get(i)){
                out+=1;
            }
        }
        return out;
    }
    // incearca produs scalar
    //https://stats.stackexchange.com/questions/510993/correlation-between-two-vectors-of-binary-values

    // nu prea merge
    private double jaccardDistance(List<Integer> vectorA, List<Integer> vectorB){
        double m11=0,m01=0,m10=0;
        for (int i=0;i<vectorA.size();i++){
            if (vectorA.get(i).equals(vectorB.get(i)) && vectorA.get(i)==1){
                m11+=1;
            }
            else if (vectorA.get(i)==0 && vectorB.get(i)==1){
                m01+=1;
            }
            else if (vectorA.get(i)==1 && vectorB.get(i)==0){
                m10+=1;
            }
        }
        // daca s la fel da 1
        System.out.println(m11+" "+m10+" "+m01);
        return (m11/(m01+m10+m11));
    }
}
