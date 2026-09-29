package com.company.platform.diagnosis;

import java.time.LocalDateTime;

// MyBatis 中 diagnosis_run 表对应的数据行对象。如果要新增“诊断执行”级别的字段，
// 需要在这里同步添加，并新增一个 Flyway 迁移脚本（V2__*.sql）来修改表结构。
public class DiagnosisRun {
    public Long id;
    public String caseId;
    public String circuitId;
    public String status;
    public String createdBy;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
}
