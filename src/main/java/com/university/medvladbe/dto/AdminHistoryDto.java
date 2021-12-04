package com.university.medvladbe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdminHistoryDto {
    private List<RegistrationResultDto> registrationResultList;
    private List<QuestionDto> questions;
}
