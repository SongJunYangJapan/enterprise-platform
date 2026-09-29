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

    // 这个事务是诊断流程的“编排接缝”：目前只创建占位步骤，
    // 后续要把每个占位步骤替换成真实的适配器调用（BOSS / LibreNMS / Dify 等），
    // 并把每一步的状态、输出、错误信息记录到 diagnosis_step 表中。
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
