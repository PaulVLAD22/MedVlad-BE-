package com.university.medvladbe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResultDto {
    private String admin;
    private String user;
    private boolean verdict;
    private String comment;
}
