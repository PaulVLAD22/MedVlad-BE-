package com.university.medvladbe.dto.request;

import lombok.*;

import java.util.*;

@Data
public class CalculateDiagnosisInput {
    private List<Integer> selectedSymptoms;
}
