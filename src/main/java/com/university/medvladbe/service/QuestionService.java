package com.university.medvladbe.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.university.medvladbe.dto.QuestionDto;
import com.university.medvladbe.dto.UserDto;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.entity.question.QuestionAnswer;
import com.university.medvladbe.entity.question.QuestionResult;
import com.university.medvladbe.repository.QuestionRepository;
import com.university.medvladbe.repository.QuestionResultRepository;
import com.university.medvladbe.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionService {
    private QuestionRepository questionRepository;
    private QuestionResultRepository questionResultRepository;
    private UserRepository userRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           QuestionResultRepository questionResultRepository,
                           UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.questionResultRepository = questionResultRepository;
        this.userRepository = userRepository;
    }

    public void acceptQuestion(long id, String adminUsername, String comment, boolean verdict) throws NotFound {
        User user = userRepository.findByUsername(adminUsername);
        Optional<Question> questionToAccept = questionRepository.findById(id);
        if (!questionToAccept.isPresent()){
            throw new NotFound();
        }

        Question question = questionToAccept.get();
        question.setChecked(true);
        questionRepository.save(question);

        QuestionResult questionResult = new QuestionResult();

        questionResult.setQuestion(questionToAccept.get());
        questionResult.setAdmin(user);
        questionResult.setComment(comment);
        questionResult.setVerdict(verdict);

        questionResultRepository.save(questionResult);
    }

    public void postQuestion(String username, String content) {
        User user = userRepository.findByUsername(username);
        Question question = Question.builder()
                .checked(false)
                .user(user)
                .content(content)
                .build();
        questionRepository.save(question);
    }

    public List<Question> getUncheckedQuestions(){
        List<Question> questions = questionRepository.findUncheckedQuestions();
        return questions;
    }

    public List<QuestionDto> getQuestions(){
        List<Question> questions = questionRepository.findActiveQuestions();
        List <QuestionDto> questionDtos = new ArrayList<>();

        questions.forEach(question -> {
            questionDtos.add(
                    QuestionDto.builder()
                    .userDto(question.getUser().userDtoFromUser())
                    .content(question.getContent())
                    .questionAnswerList(questionRepository.findAnswersForQuestion(question).stream().map(QuestionAnswer::questionAnswerDtoFromQuestionAnswer).collect(Collectors.toList()))
                    .build());
        });

        return questionDtos;
    }
}
