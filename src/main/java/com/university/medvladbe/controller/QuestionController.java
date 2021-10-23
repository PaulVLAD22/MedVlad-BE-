package com.university.medvladbe.controller;

import com.university.medvladbe.dto.QuestionDto;
import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.service.QuestionService;
import com.university.medvladbe.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        String adminUsername;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            adminUsername = ((UserDetails) principal).getUsername();
        } else {
            adminUsername = principal.toString();
        }
        try {
            questionService.acceptQuestion(id, adminUsername, comment, verdict);
            return new ResponseEntity(HttpStatus.OK);
        }catch (NotFound e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("user/postQuestion")
    public ResponseEntity<HttpStatus> postQuestion(@RequestParam String content) {
        log.info("Post Question");

        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }

        try {
            questionService.postQuestion(userName, content);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("getQuestions")
    public List<QuestionDto> getActiveQuestions() {
        return questionService.getQuestions();
    }

    @GetMapping("getInactiveQuestions")
    public List<Question> getInactiveQuestions(){
        return questionService.getUncheckedQuestions();
    }
}
