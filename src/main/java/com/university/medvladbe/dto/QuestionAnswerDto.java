package com.university.medvladbe.dto;

import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.Question;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Builder
public class QuestionAnswerDto {
    private long id;
    private UserDto doctor;
    private int numberOfLikes;
    private String content;

}