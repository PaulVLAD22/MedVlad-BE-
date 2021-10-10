package com.university.medvladbe.service;

import com.university.medvladbe.dto.QuestionDto;
import com.university.medvladbe.dto.UserDto;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.entity.question.QuestionAnswer;
import com.university.medvladbe.repository.QuestionRepository;
import com.university.medvladbe.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionService {
    private QuestionRepository questionRepository;
    private UserRepository userRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    public void postQuestion(String username, String content) {
        User user = userRepository.findByUsername(username);
        Question question = Question.builder()
                .user(user)
                .content(content)
                .build();
        questionRepository.save(question);
    }
    public List<QuestionDto> getQuestions(){
        List<Question> questions = questionRepository.findActiveQuestions();
        List <QuestionDto> questionDtos = new ArrayList<>();

        log.info(questionRepository.findAnswersForQuestion(questions.get(0)).get(0).toString());
        questions.forEach(question -> {
            questionDtos.add(
                    QuestionDto.builder()
                    .userDto(question.getUser().userDtoFromUser())
                    .content(question.getContent())
                    .questionAnswerList(questionRepository.findAnswersForQuestion(question).stream().map(QuestionAnswer::questionAnswerDtoFromQuestionAnswer).collect(Collectors.toList()))
                    .build());
        });
        //TODO:: NU MI IA ANSWER-URILE

        return questionDtos;
    }
}
