package com.company.platform.diagnosis;

import java.time.LocalDateTime;

// MyBatis persistence row. Add run-level fields here and in the next Flyway migration.
public class DiagnosisRun {
    public Long id;
    public String caseId;
    public String circuitId;
    public String status;
    public String createdBy;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
