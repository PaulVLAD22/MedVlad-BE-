package com.university.medvladbe.model.entity.question;

import com.university.medvladbe.dto.QuestionDto;
import com.university.medvladbe.model.entity.account.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private User user;
    @Column(columnDefinition = "TEXT", unique = true)
    private String content;
    private boolean checked;
    private boolean verdict=false;
    private String comment;

    @ManyToMany(cascade = {CascadeType.ALL})
    @Fetch(value = FetchMode.SELECT)
    private List<Symptom> symptoms;
    @ManyToOne
    private QuestionCategory questionCategory;
    @ManyToOne
    private User admin;
    private Date postingDate;

    public QuestionDto questionDtoFromQuestion() {
        return
                QuestionDto.builder()
                        .userDto(this.getUser().userDtoFromUser())
                        .content(this.getContent())
                        .build();
    }

}
