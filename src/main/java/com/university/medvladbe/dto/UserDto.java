package com.university.medvladbe.dto;

import com.university.medvladbe.model.entity.account.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private long id;
    private String email;
    private String firstName;
    private Role role;
    private String lastName;
    @Column(unique = true)
    private String username;
    private String profilePicture;
    private String licensePicture;
    private boolean active;
    private int points;
    private Date dateOfRegistration;
}
