package com.university.medvladbe.dto;

import lombok.*;
import lombok.experimental.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DiagnosisResultDto {
    private String condition;
    private double similarityScore;
    private UserDto similarQuestionDoctor;
}
