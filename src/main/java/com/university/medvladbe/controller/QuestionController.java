package com.university.medvladbe.controller;

import com.university.medvladbe.dto.*;
import com.university.medvladbe.dto.request.*;
import com.university.medvladbe.model.entity.question.*;
import com.university.medvladbe.exception.AlreadyLikedComment;
import com.university.medvladbe.service.QuestionService;
import com.university.medvladbe.service.UserDetailsServiceImpl;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class QuestionController {

    private UserDetailsServiceImpl userService;
    private QuestionService questionService;

    @Autowired
    public QuestionController(UserDetailsServiceImpl userService,
                              QuestionService questionService) {
        this.userService = userService;
        this.questionService = questionService;
    }

    @PostMapping("/admin/acceptQuestion")
    public ResponseEntity<HttpStatus> acceptQuestion(@RequestParam int id, @RequestParam String comment, @RequestParam boolean verdict) {
        String adminUsername = getCurrentUsername();
        try {
            questionService.acceptQuestion(id, adminUsername, comment, verdict);
            return new ResponseEntity(HttpStatus.OK);
        }catch (NotFoundException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user/postQuestion")
    public ResponseEntity<HttpStatus> postQuestion(@RequestBody PostQuestionBody postQuestionBody) {

        String userName = getCurrentUsername();

        try {
            questionService.postQuestion(userName, postQuestionBody.getContent(), "Coronavirus",postQuestionBody.getSelectedSymptoms() );
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getQuestionsForUser")
    public List<QuestionDto> getQuestionsForUser(String username){
        if (username == null){
            username = getCurrentUsername();
        }
        return questionService.getQuestionsForUser(username);
    }

    @GetMapping("/getQuestions")
    public List<QuestionDto> getActiveQuestions() {
        return questionService.getQuestions();
    }
    @GetMapping("/getCategories")
    public List<QuestionCategory> getQuestionCategory(){
        return questionService.getQuestionsCategories();
    }
    @GetMapping("/getSymptoms")
    public List<SymptomDto> getSymptoms(){
        return questionService.getSymptoms();
    }

    @GetMapping("/admin/getInactiveQuestions")
    public List<QuestionDto> getInactiveQuestions(){
        return questionService.getUncheckedQuestions();
    }

    private String getCurrentUsername(){
        String username;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(String.valueOf(auth.getAuthorities()));
        Object principal = auth.getPrincipal();
        username = principal.toString();

        return username;
    }

    @PostMapping("/doctor/postQuestionAnswer")
    public void postQuestionAnswer(@RequestParam long questionId,@RequestParam String content){
        log.info(content);
        String doctorUsername = getCurrentUsername();
        questionService.postQuestionAnswer(questionId,doctorUsername,content);
    }
    @PostMapping("/doctor/likeQuestionAnswer")
    public ResponseEntity likeQuestionAnswer(@RequestParam long questionAnswerId){
        try {
            String currentUsername = getCurrentUsername();
            questionService.likeQuestionAnswer(currentUsername, questionAnswerId);
            return ResponseEntity.ok().build();
        }catch (AlreadyLikedComment e){
            return ResponseEntity.status(452).build();
        }
    }
    @DeleteMapping("/admin/deleteQuestionAnswer")
    public void deleteQuestionAnswer(@RequestParam long questionAnswerId){
        questionService.deleteQuestionAnswer(questionAnswerId);
    }
    @DeleteMapping("/admin/deleteQuestion")
    public void deleteQuestion(@RequestParam long questionId){
        questionService.deleteQuestion(questionId);
    }

    @GetMapping("/getQuestionsForDoctor")
    public List<QuestionDto> getQuestionsForDoctor(@RequestParam String doctorUsername){
        return questionService.getQuestionsForDoctor(doctorUsername);
    }

}
