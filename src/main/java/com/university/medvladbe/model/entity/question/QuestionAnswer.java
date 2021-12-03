package com.university.medvladbe.model.entity.question;

import com.university.medvladbe.dto.QuestionAnswerDto;
import com.university.medvladbe.model.entity.account.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Data
@ToString
@Table(name = "question_answer")
public class QuestionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String pacientCondition;
    private int numberOfLikes=0;

    @OneToOne
    private Question question;

    @ManyToOne
    private User doctor;

    @Column(columnDefinition = "TEXT", length = 255)
    private String content;

    @ManyToMany(mappedBy = "answers",cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    public QuestionAnswerDto questionAnswerDtoFromQuestionAnswer() {
        return QuestionAnswerDto.builder()
                .id(this.getId())
                .numberOfLikes(this.getNumberOfLikes())
                .doctor(this.doctor.userDtoFromUser())
                .content(this.getContent())
                .condition(this.getPacientCondition())
                .build();
    }

}
