package com.university.medvladbe.service;

import com.university.medvladbe.dto.QuestionDto;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.entity.question.QuestionAnswer;
import com.university.medvladbe.repository.QuestionRepository;
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
    private UserRepository userRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,

                           UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    public void acceptQuestion(long id, String adminUsername, String comment, boolean verdict) throws NotFound {
        User admin = userRepository.findByUsername(adminUsername);
        Optional<Question> questionToAccept = questionRepository.findById(id);
        if (!questionToAccept.isPresent()) {
            throw new NotFound();
        }

        Question question = questionToAccept.get();
        question.setChecked(true);
        question.setAdmin(admin);
        question.setComment(comment);
        question.setVerdict(verdict);

        questionRepository.save(question);

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

    public List<Question> getUncheckedQuestions() {
        return questionRepository.findUncheckedQuestions();
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

    private List<QuestionDto> questionListToQuestionDtoList(List<Question> questions) {
        List<QuestionDto> questionDtos = new ArrayList<>();

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
