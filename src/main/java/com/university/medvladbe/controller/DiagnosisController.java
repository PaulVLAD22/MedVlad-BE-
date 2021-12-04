package com.university.medvladbe.controller;

import com.university.medvladbe.service.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
public class DiagnosisController {
    @Autowired
    private DiagnosisService diagnosisService;

    @GetMapping("/user/calculateDiagnosis")
    public String calculateDiagnosis(@RequestParam List<Integer> selectedSymptoms){
        return diagnosisService.calculateCondition(selectedSymptoms);
    }

}
