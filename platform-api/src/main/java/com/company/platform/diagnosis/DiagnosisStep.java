package com.company.platform.diagnosis;

import java.time.LocalDateTime;

// Preserve raw output and errors here when real adapters replace placeholders.
public class DiagnosisStep {
    public Long id;
    public Long diagnosisId;
    public String caseId;
    public String circuitId;
    public String branch;
    public String systemName;
    public String stepName;
    public String status;
    public LocalDateTime startedAt;
    public LocalDateTime finishedAt;
    public String rawOutput;
    public String errorMessage;
}
