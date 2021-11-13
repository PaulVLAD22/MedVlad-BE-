package com.university.medvladbe.entity.question;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class QuestionCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
}
