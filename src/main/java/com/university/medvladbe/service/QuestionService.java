package com.university.medvladbe.service;

import com.university.medvladbe.dto.*;
import com.university.medvladbe.model.entity.account.User;
import com.university.medvladbe.model.entity.question.*;
import com.university.medvladbe.exception.AlreadyLikedComment;
import com.university.medvladbe.repository.Questions.*;
import com.university.medvladbe.repository.UserRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionService {
    private SymptomRepository symptomRepository;
    private QuestionRepository questionRepository;
    private QuestionAnswerRepository questionAnswerRepository;
    private UserRepository userRepository;
    private QuestionCategoryRepository questionCategoryRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           UserRepository userRepository,
                           QuestionAnswerRepository questionAnswerRepository,
                           QuestionCategoryRepository questionCategoryRepository,
                           SymptomRepository symptomRepository) {
        this.questionRepository = questionRepository;
        this.questionAnswerRepository = questionAnswerRepository;
        this.userRepository = userRepository;
        this.questionCategoryRepository = questionCategoryRepository;
        this.symptomRepository = symptomRepository;
    }

    public List<SymptomDto> getSymptoms(){
        return symptomRepository.findAll().stream().map(Symptom::symptomDtoFromSymptom).collect(Collectors.toList());
    }

    public void deleteQuestion(long questionId) {
        questionRepository.deleteById(questionId);
    }

    public void deleteQuestionAnswer(long questionAnswerId) {
        questionAnswerRepository.deleteById(questionAnswerId);
    }

    public void acceptQuestion(long id, String adminUsername, String comment, boolean verdict) throws NotFoundException {
        User admin = userRepository.findByUsername(adminUsername);
        Optional<Question> questionToAccept = questionRepository.findById(id);
        if (!questionToAccept.isPresent()) {
            throw new NotFoundException("");
        }

        Question question = questionToAccept.get();
        question.setChecked(true);
        question.setAdmin(admin);
        question.setComment(comment);
        question.setVerdict(verdict);

        questionRepository.save(question);

    }

    public List<QuestionCategory> getQuestionsCategories() {
        return questionCategoryRepository.findAll();
    }

    public void postQuestion(String username, String content, String category, List<Integer> selectedSymptoms) {
        List<Symptom> symptomList = symptomRepository.findAll();
        List<Symptom> questionSymptoms = symptomList.stream()
                .filter(symptom -> selectedSymptoms.get(symptomList.indexOf(symptom)) == 1)
                .collect(Collectors.toList());
        User user = userRepository.findByUsername(username);
        QuestionCategory questionCategory = questionCategoryRepository.findByName(category);
        Question question = Question.builder()
                .checked(false)
                .user(user)
                .content(content)
                .symptoms(questionSymptoms)
                .questionCategory(questionCategory)
                .postingDate(new Date(System.currentTimeMillis()))
                .build();
        questionRepository.save(question);
    }

    public List<QuestionDto> getUncheckedQuestions() {
        List<Question> questions = questionRepository.findUncheckedQuestions();
        return questionListToQuestionDtoList(questions);
    }

    public List<QuestionDto> getQuestionsForDoctor(String username) {
        User doctor = userRepository.findByUsername(username);
        List<Question> questions = questionRepository.findActiveQuestions()
                .stream().filter(question -> {
                    List<QuestionAnswer> questionAnswers = questionAnswerRepository.findByQuestion(question);
                    List<QuestionAnswer> answeredByDoctor = questionAnswers.stream().filter(questionAnswer -> questionAnswer.getDoctor().equals(doctor)).collect(Collectors.toList());
                    return answeredByDoctor.size() != 0;
                }).collect(Collectors.toList());

        return questionListToQuestionDtoList(questions);
    }

    public List<QuestionDto> getQuestionsForUser(String username) {
        List<Question> questions = questionRepository.findActiveQuestions()
                .stream().filter(question -> question.getUser().getUsername().equals(username))
                .collect(Collectors.toList());

        return questionListToQuestionDtoList(questions);
    }

    public List<QuestionDto> getQuestions() {
        List<Question> questions = questionRepository.findActiveQuestions();
        return questionListToQuestionDtoList(questions);
    }


    public void postQuestionAnswer(long questionId, String doctorUsername, String content) {
        Question question = questionRepository.findById(questionId).get();// poate nu e ok

        QuestionAnswer questionAnswer = QuestionAnswer.builder()
                .question(question)
                .doctor(userRepository.findByUsername(doctorUsername))
                .content(content).build();
        questionAnswerRepository.save(questionAnswer);
    }

    public List<QuestionDto> questionListToQuestionDtoList(List<Question> questions) {
        List<QuestionDto> questionDtos = new ArrayList<>();

        questions.forEach(question -> {
            questionDtos.add(
                    QuestionDto.builder()
                            .id(question.getId())
                            .userDto(question.getUser().userDtoFromUser())
                            .content(question.getContent())
                            .questionAnswerList(questionRepository.findAnswersForQuestion(question).stream().map(QuestionAnswer::questionAnswerDtoFromQuestionAnswer).collect(Collectors.toList()))
                            .comment(question.getComment())
                            .verdict(question.isVerdict())
                            .postingDate(question.getPostingDate())
                            .symptoms(question.getSymptoms().stream().map(Symptom::symptomDtoFromSymptom).collect(Collectors.toList()))
                            .build());
        });
        return questionDtos;
    }

    public void likeQuestionAnswer(String username, long questionAnswerId) {
        QuestionAnswer questionAnswer = questionAnswerRepository.findById(questionAnswerId).get();
        User user = userRepository.findByUsername(username);

        if (!user.getAnswers().contains(questionAnswer)) {
            user.getAnswers().add(questionAnswer);
            questionAnswer.setNumberOfLikes(questionAnswer.getNumberOfLikes() + 1);
            userRepository.save(user);
        } else {
            throw new AlreadyLikedComment();
        }
    }

}
