package com.university.medvladbe.model.entity.question;

import com.university.medvladbe.dto.*;
import com.university.medvladbe.repository.Questions.*;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "symptom")
public class Symptom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @ManyToMany(mappedBy = "symptoms", cascade = CascadeType.ALL)
    private List<Question> questions;

    public SymptomDto symptomDtoFromSymptom() {
        return SymptomDto.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }

}
