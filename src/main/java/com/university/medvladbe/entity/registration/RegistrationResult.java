package com.university.medvladbe.entity.registration;

import com.university.medvladbe.entity.account.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="registration_result")
public class RegistrationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User admin;
    @ManyToOne
    private User user;
    private boolean verdict;
    private String comment;
}
