package com.university.medvladbe.dto;

import com.university.medvladbe.entity.account.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResultDto {
    private UserDto admin;
    private UserDto user;
    private boolean verdict;
    private String comment;
}
