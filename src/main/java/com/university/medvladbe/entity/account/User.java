package com.university.medvladbe.entity.account;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    @ManyToMany(fetch=EAGER)
    private Collection<Role> roles = new ArrayList<>();
    private String lastName;
    @Column(unique = true)
    private String username;
    private String password;
    private String profilePicture;
    private boolean active;
    private long token;
    private int adminPoints;
    private int doctorPoints;
    private Date dateOfRegistration;
}