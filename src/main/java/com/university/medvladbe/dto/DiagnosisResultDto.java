package com.university.medvladbe.dto;

import lombok.*;
import lombok.experimental.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DiagnosisResultDto {
    private String condition;
    private int similarityScore;
    private UserDto similarQuestionDoctor;
}
