package com.university.medvladbe.service;

import com.university.medvladbe.dto.QuestionDto;
import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.entity.question.QuestionAnswer;
import com.university.medvladbe.exception.AlreadyLikedComment;
import com.university.medvladbe.repository.QuestionAnswerRepository;
import com.university.medvladbe.repository.QuestionRepository;
import com.university.medvladbe.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionService {
    private QuestionRepository questionRepository;
    private QuestionAnswerRepository questionAnswerRepository;
    private UserRepository userRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           UserRepository userRepository,
                           QuestionAnswerRepository questionAnswerRepository) {
        this.questionRepository = questionRepository;
        this.questionAnswerRepository = questionAnswerRepository;
        this.userRepository = userRepository;
    }

    public void deleteQuestion(long questionId){
        questionRepository.deleteById(questionId);
    }
    public void deleteQuestionAnswer(long questionAnswerId){
        questionAnswerRepository.deleteById(questionAnswerId);
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
                .postingDate(new Date(System.currentTimeMillis()))
                .build();
        questionRepository.save(question);
    }

    public List<QuestionDto> getUncheckedQuestions() {
        List<Question> questions = questionRepository.findUncheckedQuestions();
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
                            .postingDate(question.getPostingDate())
                            .questionAnswerList(questionRepository.findAnswersForQuestion(question).stream().map(QuestionAnswer::questionAnswerDtoFromQuestionAnswer).collect(Collectors.toList()))
                            .build());
        });
        return questionDtos;
    }

    public void likeQuestionAnswer(String username, long questionAnswerId){
        QuestionAnswer questionAnswer = questionAnswerRepository.findById(questionAnswerId).get();
        User user = userRepository.findByUsername(username);

        if (!user.getAnswers().contains(questionAnswer)) {
            user.getAnswers().add(questionAnswer);
            questionAnswer.setNumberOfLikes(questionAnswer.getNumberOfLikes() + 1);
            userRepository.save(user);
        }
        else{
            throw new AlreadyLikedComment();
        }
    }

}
