package com.university.medvladbe.dto;

import com.university.medvladbe.model.entity.question.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {
    private long id;
    private UserDto userDto;
    private String content;
    private List<QuestionAnswerDto> questionAnswerList;
    private QuestionCategory questionCategory;
    private Date postingDate;
    private boolean verdict;
    private String comment;
}
