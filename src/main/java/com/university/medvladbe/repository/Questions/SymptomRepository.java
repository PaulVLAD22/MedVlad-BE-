package com.university.medvladbe.repository.Questions;

import com.university.medvladbe.model.entity.question.*;
import org.springframework.data.jpa.repository.*;

public interface SymptomRepository extends JpaRepository<Symptom,Long> {
}
