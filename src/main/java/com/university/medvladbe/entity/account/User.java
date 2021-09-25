package com.university.medvladbe.entity.account;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    @ManyToOne
    private Role role;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String profilePicture;
    private boolean active;
    private long token;
    private int adminPoints;
    private int doctorPoints;
    private Date dateOfRegistration;
}