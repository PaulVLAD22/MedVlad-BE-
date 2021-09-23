package com.university.medvladbe.entity.question;

import com.university.medvladbe.entity.account.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="question_result")
public class QuestionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User admin;
    @OneToOne
    private Question question;
    private boolean verdict;
    private String comment;
}
