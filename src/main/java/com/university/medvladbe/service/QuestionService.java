package com.university.medvladbe.service;

import com.university.medvladbe.dto.*;
import com.university.medvladbe.exception.*;
import com.university.medvladbe.model.entity.account.*;
import com.university.medvladbe.model.entity.question.*;
import com.university.medvladbe.repository.Questions.*;
import com.university.medvladbe.repository.*;
import javassist.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.sql.Date;
import java.util.*;
import java.util.stream.*;

@Service
@Slf4j
public class QuestionService {
    private SymptomRepository symptomRepository;
    private QuestionRepository questionRepository;
    private QuestionAnswerRepository questionAnswerRepository;
    private UserRepository userRepository;


    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           UserRepository userRepository,
                           QuestionAnswerRepository questionAnswerRepository,
                           SymptomRepository symptomRepository) {
        this.questionRepository = questionRepository;
        this.questionAnswerRepository = questionAnswerRepository;
        this.userRepository = userRepository;
        this.symptomRepository = symptomRepository;
    }


    public List<SymptomDto> getSymptoms() {
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
        log.info("AICI");
        Optional<Question> questionToAccept = questionRepository.findById(id);
        if (!questionToAccept.isPresent()) {
            throw new NotFoundException("");
        }
        log.info("AICI");

        Question question = questionToAccept.get();
        question.setChecked(true);
        question.setAdmin(admin);
        question.setComment(comment);
        question.setVerdict(verdict);

        questionRepository.save(question);

    }

    public void postQuestion(String username, String content, String category, List<Integer> selectedSymptoms) {
        List<Symptom> symptomList = symptomRepository.findAll();
        List<Symptom> questionSymptoms = symptomList.stream()
                .filter(symptom -> selectedSymptoms.get(symptomList.indexOf(symptom)) == 1)
                .collect(Collectors.toList());
        User user = userRepository.findByUsername(username);

        Question question = Question.builder()
                .checked(false)
                .user(user)
                .content(content)
                .symptoms(questionSymptoms)
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


    public void postQuestionAnswer(long questionId, String doctorUsername, String content, String condition) {
        Question question = questionRepository.findById(questionId).get();// poate nu e ok

        QuestionAnswer questionAnswer = QuestionAnswer.builder()
                .question(question)
                .doctor(userRepository.findByUsername(doctorUsername))
                .content(content)
                .pacientCondition(condition)
                .build();
        question.setAnswer(questionAnswer);
        questionRepository.save(question);
    }

    public List<QuestionDto> questionListToQuestionDtoList(List<Question> questions) {
        List<QuestionDto> questionDtos = new ArrayList<>();

        questions.forEach(question -> {
            QuestionAnswerDto questionAnswerDto = null;
            if (question.getAnswer() != null) {
                questionAnswerDto = question.getAnswer().questionAnswerDtoFromQuestionAnswer();
            }
            questionDtos.add(
                    QuestionDto.builder()
                            .id(question.getId())
                            .userDto(question.getUser().userDtoFromUser())
                            .content(question.getContent())
                            .answer(questionAnswerDto)
                            .comment(question.getComment())
                            .verdict(question.isVerdict())
                            .postingDate(question.getPostingDate())
                            .symptoms(question.getSymptoms().stream().map(Symptom::symptomDtoFromSymptom).collect(Collectors.toList()))
                            .build());
        });
        log.info("DA@2");
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
