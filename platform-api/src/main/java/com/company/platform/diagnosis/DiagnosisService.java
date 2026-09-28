package com.company.platform.diagnosis;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class DiagnosisService {
    private final DiagnosisMapper mapper;
    public DiagnosisService(DiagnosisMapper mapper) { this.mapper = mapper; }

    // This transaction is the orchestration seam: replace each placeholder with
    // an adapter call later, recording status/output/error in diagnosis_step.
    @Transactional
    public DiagnosisView create(String circuitId, String username) {
        var run = new DiagnosisRun();
        run.caseId = UUID.randomUUID().toString();
        run.circuitId = circuitId.trim();
        run.status = "PENDING";
        run.createdBy = username;
        run.createdAt = LocalDateTime.now();
        run.updatedAt = run.createdAt;
        mapper.insertRun(run);
        for (String branch : List.of("SW", "PE")) {
            var step = new DiagnosisStep();
            step.diagnosisId = run.id;
            step.caseId = run.caseId;
            step.circuitId = run.circuitId;
            step.branch = branch;
            step.systemName = "PLACEHOLDER";
            step.stepName = branch + " diagnosis pending integration";
            step.status = "PENDING";
            mapper.insertStep(step);
        }
        return get(run.id);
    }

    @Transactional(readOnly = true)
    public DiagnosisView get(long id) {
        var run = mapper.findRun(id);
        if (run == null) throw new ResponseStatusException(NOT_FOUND, "Diagnosis not found");
        return new DiagnosisView(run.id, run.caseId, run.circuitId, run.status,
            run.createdBy, run.createdAt, mapper.findSteps(id));
    }

    @Transactional(readOnly = true)
    public List<DiagnosisView> history(String circuitId) {
        return mapper.findByCircuitId(circuitId.trim()).stream().map(run -> get(run.id)).toList();
    }
}
