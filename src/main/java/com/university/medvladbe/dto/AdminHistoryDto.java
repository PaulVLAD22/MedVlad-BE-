package com.university.medvladbe.dto;

import com.university.medvladbe.entity.question.Question;
import com.university.medvladbe.entity.registration.RegistrationResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Builder
public class AdminHistoryDto {
    private List<RegistrationResult> registrationResultList;
    private List<Question> questions;
}
