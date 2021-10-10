package com.university.medvladbe.dto;

import com.university.medvladbe.entity.account.User;
import com.university.medvladbe.entity.question.QuestionAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {
    private UserDto userDto;
    private String content;
    private List<QuestionAnswerDto> questionAnswerList;
}
