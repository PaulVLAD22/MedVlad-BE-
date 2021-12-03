package com.university.medvladbe.dto;

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
    private List<SymptomDto> symptoms;
    private QuestionAnswerDto answer;
    private Date postingDate;
    private boolean verdict;
    private String comment;
}
