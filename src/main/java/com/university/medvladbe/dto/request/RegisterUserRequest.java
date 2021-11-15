package com.university.medvladbe.dto.request;

import lombok.Data;
import lombok.Getter;

@Data
public class RegisterUserRequest {
    private String email;
    private String username;
    private String password;
    private String licensePicture;
    private String role;
}
