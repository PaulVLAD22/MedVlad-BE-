package com.university.medvladbe.dto;

import com.university.medvladbe.entity.account.Role;
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
    private String firstName;
    @ManyToOne
    private Role role;
    private String lastName;
    @Column(unique = true)
    private String username;
    private String profilePicture;
    private boolean active;
    private long token;
    private int adminPoints;
    private int doctorPoints;
    private Date dateOfRegistration;
}
