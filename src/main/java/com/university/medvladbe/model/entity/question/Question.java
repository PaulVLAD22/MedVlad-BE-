package com.university.medvladbe.model.entity.question;

import com.university.medvladbe.dto.*;
import com.university.medvladbe.model.entity.account.User;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
@Builder
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @ToString.Exclude
    private User user;
    @Column(columnDefinition = "TEXT", unique = true)
    private String content;
    private boolean checked;
    private boolean verdict=false;
    private String comment;

    @OneToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private QuestionAnswer answer;

    @ManyToMany(cascade = {CascadeType.ALL})
    @Fetch(value = FetchMode.SELECT)
    @ToString.Exclude
    private List<Symptom> symptoms;

    @ManyToOne
    @ToString.Exclude
    private User admin;
    private Date postingDate;


}
