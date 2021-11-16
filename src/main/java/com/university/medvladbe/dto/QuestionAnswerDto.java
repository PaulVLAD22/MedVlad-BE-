package com.university.medvladbe.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

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