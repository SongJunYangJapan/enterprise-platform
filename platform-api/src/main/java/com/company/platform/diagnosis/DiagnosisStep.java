package com.company.platform.diagnosis;

import java.time.LocalDateTime;

// diagnosis_step 表对应的数据行对象。等真实的适配器替换掉占位步骤后，
// 外部系统返回的原始输出（rawOutput）和错误信息（errorMessage）就保存在这里。
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
