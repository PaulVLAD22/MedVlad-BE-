package com.university.medvladbe.entity.question;

import com.university.medvladbe.dto.QuestionAnswerDto;
import com.university.medvladbe.entity.account.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @ManyToOne
    private Question question;
    @ManyToOne
    private User doctor;
    private int numberOfLikes=0;
    private String content;

    @ManyToMany(mappedBy = "likedAnswers")
    private List<User> usersLikingAnswer = new ArrayList<>();

    public QuestionAnswerDto questionAnswerDtoFromQuestionAnswer() {
        return QuestionAnswerDto.builder()
                .id(this.getId())
                .numberOfLikes(this.getNumberOfLikes())
                .doctor(this.doctor.userDtoFromUser())
                .content(this.getContent())
                .build();
    }

}
