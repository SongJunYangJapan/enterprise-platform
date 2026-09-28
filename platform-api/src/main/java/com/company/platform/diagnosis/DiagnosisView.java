package com.company.platform.diagnosis;

import java.time.LocalDateTime;
import java.util.List;

public record DiagnosisView(Long diagnosisId, String caseId, String circuitId, String status,
    String createdBy, LocalDateTime createdAt, List<DiagnosisStep> steps) {}
