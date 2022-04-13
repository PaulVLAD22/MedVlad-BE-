package com.university.medvladbe.controller;

import com.university.medvladbe.dto.*;
import com.university.medvladbe.dto.request.*;
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

    @PostMapping("/user/calculateDiagnosis")
    public List<DiagnosisResultDto> calculateDiagnosis(@RequestBody CalculateDiagnosisInput calculateDiagnosisInput){
        return diagnosisService.calculateCondition(calculateDiagnosisInput.getSelectedSymptoms());
    }

}
