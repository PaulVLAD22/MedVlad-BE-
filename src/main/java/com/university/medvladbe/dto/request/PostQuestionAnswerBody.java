package com.university.medvladbe.dto.request;

import lombok.*;

@Data
public class PostQuestionAnswerBody {
    private long questionId;
    private String comment;
    private String condition;
}
