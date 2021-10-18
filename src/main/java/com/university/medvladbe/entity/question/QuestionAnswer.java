package com.university.medvladbe.entity.question;

import com.university.medvladbe.dto.QuestionAnswerDto;
import com.university.medvladbe.entity.account.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

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
    //TODO:: s ar putea sa vrei questionResult aici, nu question
    @ManyToOne
    private User doctor;
    private int numberOfLikes;
    private String content;

    public QuestionAnswerDto questionAnswerDtoFromQuestionAnswer() {
        return QuestionAnswerDto.builder()
                .numberOfLikes(this.getNumberOfLikes())
                .doctor(this.doctor.userDtoFromUser())
                .content(this.getContent())
                .build();
    }

}
