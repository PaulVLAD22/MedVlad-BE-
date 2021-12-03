package com.university.medvladbe.dto.request;

import lombok.*;

import java.util.*;

@Data
public class PostQuestionBody {
    private String content;
    private List<Integer> selectedSymptoms;
}
